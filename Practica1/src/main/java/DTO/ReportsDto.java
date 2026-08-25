package DTO;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import models.Appointment;
import models.Doctor;
import models.LogEntry;
import models.Patient;

public class ReportsDto {

    private final String logFilePath = "data/logs.dat";
    private final String reportsFolder = "data/reports";

    // Tamaños para el archivo binario de logs
    private static final int LOG_ID_SIZE = 36;
    private static final int LOG_DATETIME_SIZE = 19; // yyyy-MM-dd HH:mm:ss
    private static final int LOG_USER_SIZE = 60;
    private static final int LOG_MODULE_SIZE = 30;
    private static final int LOG_ACTION_SIZE = 35;
    private static final int LOG_DETAILS_SIZE = 150;
    private static final int LOG_REGISTER_SIZE = LOG_ID_SIZE + LOG_DATETIME_SIZE + LOG_USER_SIZE
            + LOG_MODULE_SIZE + LOG_ACTION_SIZE + LOG_DETAILS_SIZE + 1; // + \n

    private final DateTimeFormatter logDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

    // ==========================================
    // GESTIÓN DE CARPETAS Y EXPORTACIÓN
    // ==========================================

    public void createFolder() throws IOException {
        File folder = new File(reportsFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public void openFolder() {
        File folder = new File(reportsFolder);
        try {
            if (!folder.exists()) {
                folder.mkdirs();
            }
            Desktop.getDesktop().open(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exporta un conjunto de datos tabulares a formato CSV con codificación UTF-8 (BOM).
     */
    public File exportToCSV(String reportTitle, String[] headers, ArrayList<Object[]> rows) throws IOException {
        createFolder();
        String cleanTitle = reportTitle.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File file = new File(reportsFolder, cleanTitle + "_" + timestamp + ".csv");

        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)) {

            // BOM para compatibilidad con Microsoft Excel
            fos.write(0xEF);
            fos.write(0xBB);
            fos.write(0xBF);

            // Título del reporte
            writer.write("\"REPORTE: " + reportTitle + "\"");
            writer.newLine();
            writer.write("\"Fecha de Generación: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\"");
            writer.newLine();
            writer.newLine();

            // Encabezados
            if (headers != null) {
                for (int i = 0; i < headers.length; i++) {
                    writer.write("\"" + headers[i].replace("\"", "\"\"") + "\"");
                    if (i < headers.length - 1) writer.write(",");
                }
                writer.newLine();
            }

            // Filas
            if (rows != null) {
                for (Object[] row : rows) {
                    for (int i = 0; i < row.length; i++) {
                        String value = (row[i] != null) ? row[i].toString() : "";
                        writer.write("\"" + value.replace("\"", "\"\"") + "\"");
                        if (i < row.length - 1) writer.write(",");
                    }
                    writer.newLine();
                }
            }
        }

        return file;
    }

    // ==========================================
    // SISTEMA DE AUDITORÍA Y BITÁCORA DE LOGS
    // ==========================================

    /**
     * Registra una interacción de usuario en el archivo binario de logs.
     */
    public static synchronized void recordLog(String user, String module, String action, String details) {
        try {
            File dir = new File("data");
            if (!dir.exists()) dir.mkdirs();

            File logFile = new File("data/logs.dat");
            RandomAccessFile raf = new RandomAccessFile(logFile, "rw");
            raf.seek(raf.length());

            String id = UUID.randomUUID().toString();
            String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String idFmt = String.format("%-36s", id);
            String dtFmt = String.format("%-19s", dt);
            String userFmt = String.format("%-60s", user != null ? user : "Sistema");
            String modFmt = String.format("%-30s", module != null ? module : "General");
            String actFmt = String.format("%-35s", action != null ? action : "Acción");
            String detFmt = String.format("%-150s", details != null ? details : "");

            raf.write(idFmt.getBytes(StandardCharsets.ISO_8859_1));
            raf.write(dtFmt.getBytes(StandardCharsets.ISO_8859_1));
            raf.write(userFmt.getBytes(StandardCharsets.ISO_8859_1));
            raf.write(modFmt.getBytes(StandardCharsets.ISO_8859_1));
            raf.write(actFmt.getBytes(StandardCharsets.ISO_8859_1));
            raf.write(detFmt.getBytes(StandardCharsets.ISO_8859_1));
            raf.writeBytes("\n");

            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lee y filtra todos los registros de la bitácora de logs.
     */
    public ArrayList<LogEntry> readLogs(String moduleFilter, String actionFilter) {
        ArrayList<LogEntry> logs = new ArrayList<>();
        File file = new File(logFilePath);

        if (!file.exists() || file.length() == 0) {
            initDefaultLogs();
        }

        try (RandomAccessFile raf = new RandomAccessFile(logFilePath, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                if (raf.getFilePointer() + 330 > raf.length()) {
                    break;
                }

                byte[] idBytes = new byte[LOG_ID_SIZE];
                byte[] dtBytes = new byte[LOG_DATETIME_SIZE];
                byte[] userBytes = new byte[LOG_USER_SIZE];
                byte[] modBytes = new byte[LOG_MODULE_SIZE];
                byte[] actBytes = new byte[LOG_ACTION_SIZE];
                byte[] detBytes = new byte[LOG_DETAILS_SIZE];

                raf.readFully(idBytes);
                raf.readFully(dtBytes);
                raf.readFully(userBytes);
                raf.readFully(modBytes);
                raf.readFully(actBytes);
                raf.readFully(detBytes);

                byte b = raf.readByte();
                if (b == '\r') {
                    if (raf.getFilePointer() < raf.length()) {
                        long mark = raf.getFilePointer();
                        if (raf.readByte() != '\n') {
                            raf.seek(mark);
                        }
                    }
                }

                String id = new String(idBytes, StandardCharsets.ISO_8859_1).trim();
                String dtStr = new String(dtBytes, StandardCharsets.ISO_8859_1).trim();
                String user = new String(userBytes, StandardCharsets.ISO_8859_1).trim();
                String module = new String(modBytes, StandardCharsets.ISO_8859_1).trim();
                String action = new String(actBytes, StandardCharsets.ISO_8859_1).trim();
                String details = new String(detBytes, StandardCharsets.ISO_8859_1).trim();

                if (id.isEmpty()) continue;

                LocalDateTime ldt;
                try {
                    ldt = LocalDateTime.parse(dtStr, logDateTimeFormatter);
                } catch (Exception ex) {
                    ldt = LocalDateTime.now();
                }

                LogEntry entry = new LogEntry(id, ldt, user, module, action, details);

                // Filtro por módulo
                if (moduleFilter != null && !moduleFilter.equalsIgnoreCase("Todos")) {
                    if (!entry.getModule().toLowerCase().contains(moduleFilter.toLowerCase())) {
                        continue;
                    }
                }

                // Filtro por acción
                if (actionFilter != null && !actionFilter.equalsIgnoreCase("Todas")) {
                    if (!entry.getAction().toLowerCase().contains(actionFilter.toLowerCase())) {
                        continue;
                    }
                }

                logs.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ordenar descendentemente por fecha (los más recientes primero)
        logs.sort((a, b) -> b.getDateTime().compareTo(a.getDateTime()));
        return logs;
    }

    private void initDefaultLogs() {
        recordLog("Administrador (0000000000000)", "Autenticación", "Login", "Inicio de sesión inicial del sistema");
        recordLog("Administrador (0000000000000)", "Pacientes", "Creación", "Carga inicial de registros de pacientes");
        recordLog("Administrador (0000000000000)", "Médicos", "Creación", "Carga inicial de registros de médicos");
        recordLog("Administrador (0000000000000)", "Citas", "Creación", "Inicialización de citas médicas del sistema");
    }

    // ==========================================
    // REPORTES DE PACIENTES
    // ==========================================

    /**
     * 1. Reporte completo de pacientes.
     */
    public ArrayList<Patient> reportPatientsComplete() {
        PatientsDto dto = new PatientsDto();
        ArrayList<Patient> list = dto.readAll();
        return (list != null) ? list : new ArrayList<>();
    }

    /**
     * 2. Reporte de pacientes por tipo de sangre.
     */
    public ArrayList<Patient> reportPatientsByBloodType(String bloodType) {
        ArrayList<Patient> all = reportPatientsComplete();
        if (bloodType == null || bloodType.equalsIgnoreCase("Todos")) {
            return all;
        }

        ArrayList<Patient> filtered = new ArrayList<>();
        String target = bloodType.trim().toUpperCase();
        for (Patient p : all) {
            if (p.getBloodType() != null && p.getBloodType().trim().equalsIgnoreCase(target)) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    /**
     * 3. Reporte de pacientes con mayor cantidad de citas.
     * Retorna filas: [ID, Nombre Paciente, Total Citas, Programadas, Atendidas, Canceladas]
     */
    public ArrayList<Object[]> reportPatientsMostAppointments() {
        ArrayList<Patient> patients = reportPatientsComplete();
        AppointmentsDto appointmentsDto = new AppointmentsDto();
        ArrayList<Appointment> appointments = appointmentsDto.readAll();
        if (appointments == null) appointments = new ArrayList<>();

        Map<String, int[]> stats = new HashMap<>(); // [total, programadas, atendidas, canceladas]
        for (Appointment a : appointments) {
            String pid = a.getIdPatient();
            if (pid == null) continue;
            stats.putIfAbsent(pid, new int[4]);
            int[] s = stats.get(pid);
            s[0]++; // total
            if (AppointmentsDto.STATE_PROGRAMADA.equalsIgnoreCase(a.getState())) s[1]++;
            else if (AppointmentsDto.STATE_ATENDIDA.equalsIgnoreCase(a.getState())) s[2]++;
            else if (AppointmentsDto.STATE_CANCELADA.equalsIgnoreCase(a.getState())) s[3]++;
        }

        ArrayList<Object[]> result = new ArrayList<>();
        for (Patient p : patients) {
            int[] s = stats.getOrDefault(p.getId(), new int[]{0, 0, 0, 0});
            result.add(new Object[]{
                p.getId(),
                p.getName() + " " + p.getLastname(),
                s[0],
                s[1],
                s[2],
                s[3]
            });
        }

        // Orden descendente por total de citas
        result.sort((a, b) -> Integer.compare((int) b[2], (int) a[2]));
        return result;
    }

    /**
     * 4. Reporte de pacientes que nunca han tenido una cita.
     */
    public ArrayList<Patient> reportPatientsNoAppointments() {
        ArrayList<Patient> patients = reportPatientsComplete();
        AppointmentsDto appointmentsDto = new AppointmentsDto();
        ArrayList<Appointment> appointments = appointmentsDto.readAll();
        if (appointments == null) appointments = new ArrayList<>();

        Set<String> patientIdsWithAppointments = new HashSet<>();
        for (Appointment a : appointments) {
            if (a.getIdPatient() != null && !a.getIdPatient().isBlank()) {
                patientIdsWithAppointments.add(a.getIdPatient().trim());
            }
        }

        ArrayList<Patient> result = new ArrayList<>();
        for (Patient p : patients) {
            if (!patientIdsWithAppointments.contains(p.getId().trim())) {
                result.add(p);
            }
        }
        return result;
    }

    // ==========================================
    // REPORTES DE MÉDICOS
    // ==========================================

    /**
     * 1. Reporte completo de médicos.
     */
    public ArrayList<Doctor> reportDoctorsComplete() {
        DoctorsDto dto = new DoctorsDto();
        ArrayList<Doctor> list = dto.readAll();
        return (list != null) ? list : new ArrayList<>();
    }

    /**
     * 2. Reporte de médicos por especialidad.
     */
    public ArrayList<Doctor> reportDoctorsBySpeciality(String speciality) {
        ArrayList<Doctor> all = reportDoctorsComplete();
        if (speciality == null || speciality.isBlank() || speciality.equalsIgnoreCase("Todas")) {
            return all;
        }

        String search = speciality.trim().toLowerCase();
        ArrayList<Doctor> filtered = new ArrayList<>();
        for (Doctor d : all) {
            if (d.getSpeciality() != null && d.getSpeciality().toLowerCase().contains(search)) {
                filtered.add(d);
            }
        }
        return filtered;
    }

    /**
     * 3. Reporte de médicos con mayor cantidad de citas.
     * Retorna filas: [UUID Médico, Nombre Médico, Especialidad, Total Citas, Citas Atendidas, Citas Programadas]
     */
    public ArrayList<Object[]> reportDoctorsMostAppointments() {
        ArrayList<Doctor> doctors = reportDoctorsComplete();
        AppointmentsDto appointmentsDto = new AppointmentsDto();
        ArrayList<Appointment> appointments = appointmentsDto.readAll();
        if (appointments == null) appointments = new ArrayList<>();

        Map<String, int[]> stats = new HashMap<>(); // [total, atendidas, programadas, canceladas]
        for (Appointment a : appointments) {
            String did = a.getIdDoctor();
            if (did == null) continue;
            stats.putIfAbsent(did, new int[4]);
            int[] s = stats.get(did);
            s[0]++; // total
            if (AppointmentsDto.STATE_ATENDIDA.equalsIgnoreCase(a.getState())) s[1]++;
            else if (AppointmentsDto.STATE_PROGRAMADA.equalsIgnoreCase(a.getState())) s[2]++;
            else if (AppointmentsDto.STATE_CANCELADA.equalsIgnoreCase(a.getState())) s[3]++;
        }

        ArrayList<Object[]> result = new ArrayList<>();
        for (Doctor d : doctors) {
            int[] s = stats.getOrDefault(d.getId(), new int[]{0, 0, 0, 0});
            result.add(new Object[]{
                d.getId(),
                d.getName() + " " + d.getLastname(),
                d.getSpeciality(),
                s[0],
                s[1],
                s[2]
            });
        }

        // Orden descendente por total de citas
        result.sort((a, b) -> Integer.compare((int) b[3], (int) a[3]));
        return result;
    }

    /**
     * 4. Reporte de médicos con citas programadas para una fecha específica.
     * Retorna filas: [UUID Médico, Nombre Médico, Especialidad, Fecha, Hora Cita, Paciente, Motivo]
     */
    public ArrayList<Object[]> reportDoctorsWithAppointmentsOnDate(LocalDate targetDate) {
        ArrayList<Object[]> result = new ArrayList<>();
        if (targetDate == null) return result;

        AppointmentsDto appointmentsDto = new AppointmentsDto();
        DoctorsDto doctorsDto = new DoctorsDto();
        PatientsDto patientsDto = new PatientsDto();

        ArrayList<Appointment> appointments = appointmentsDto.readAppointmentsByDate(targetDate);
        if (appointments == null) return result;

        for (Appointment a : appointments) {
            Doctor doctor = doctorsDto.searchRegister(a.getIdDoctor());
            Patient patient = patientsDto.searchRegister(a.getIdPatient());

            String doctorName = (doctor != null) ? doctor.getName() + " " + doctor.getLastname() : a.getIdDoctor();
            String doctorSpec = (doctor != null) ? doctor.getSpeciality() : "N/A";
            String patientName = (patient != null) ? patient.getName() + " " + patient.getLastname() + " (" + patient.getId() + ")" : a.getIdPatient();

            String formattedDate = (a.getDate() != null) ? a.getDate().format(dateFormatter) : "";
            String formattedHour = (a.getHour() != null) ? a.getHour().format(timeFormatter) : "";

            result.add(new Object[]{
                a.getIdDoctor(),
                doctorName,
                doctorSpec,
                formattedDate,
                formattedHour,
                patientName,
                a.getConsultationReason()
            });
        }

        // Orden ascendente por hora de cita
        result.sort(Comparator.comparing(r -> r[4].toString()));
        return result;
    }

    // ==========================================
    // REPORTES DE CITAS
    // ==========================================

    /**
     * 1. Reporte completo de citas.
     */
    public ArrayList<Appointment> reportAppointmentsComplete() {
        AppointmentsDto dto = new AppointmentsDto();
        ArrayList<Appointment> list = dto.readAll();
        return (list != null) ? list : new ArrayList<>();
    }

    /**
     * 2. Reporte de citas por rango de fechas.
     */
    public ArrayList<Appointment> reportAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        ArrayList<Appointment> all = reportAppointmentsComplete();
        ArrayList<Appointment> filtered = new ArrayList<>();

        for (Appointment a : all) {
            if (a.getDate() == null) continue;
            boolean afterOrEqualStart = (startDate == null) || !a.getDate().isBefore(startDate);
            boolean beforeOrEqualEnd = (endDate == null) || !a.getDate().isAfter(endDate);

            if (afterOrEqualStart && beforeOrEqualEnd) {
                filtered.add(a);
            }
        }

        filtered.sort(Comparator.comparing(Appointment::getDate));
        return filtered;
    }

    /**
     * 3. Reporte de citas por médico.
     */
    public ArrayList<Appointment> reportAppointmentsByDoctor(String query) {
        ArrayList<Appointment> all = reportAppointmentsComplete();
        if (query == null || query.isBlank()) return all;

        String search = query.trim().toLowerCase();
        DoctorsDto doctorsDto = new DoctorsDto();
        ArrayList<Appointment> filtered = new ArrayList<>();

        for (Appointment a : all) {
            if (a.getIdDoctor().toLowerCase().contains(search)) {
                filtered.add(a);
                continue;
            }
            Doctor d = doctorsDto.searchRegister(a.getIdDoctor());
            if (d != null) {
                String fullName = (d.getName() + " " + d.getLastname()).toLowerCase();
                String spec = d.getSpeciality().toLowerCase();
                if (fullName.contains(search) || spec.contains(search)) {
                    filtered.add(a);
                }
            }
        }
        return filtered;
    }

    /**
     * 4. Reporte de citas por paciente.
     */
    public ArrayList<Appointment> reportAppointmentsByPatient(String query) {
        ArrayList<Appointment> all = reportAppointmentsComplete();
        if (query == null || query.isBlank()) return all;

        String search = query.trim().toLowerCase();
        PatientsDto patientsDto = new PatientsDto();
        ArrayList<Appointment> filtered = new ArrayList<>();

        for (Appointment a : all) {
            if (a.getIdPatient().toLowerCase().contains(search)) {
                filtered.add(a);
                continue;
            }
            Patient p = patientsDto.searchRegister(a.getIdPatient());
            if (p != null) {
                String fullName = (p.getName() + " " + p.getLastname()).toLowerCase();
                if (fullName.contains(search)) {
                    filtered.add(a);
                }
            }
        }
        return filtered;
    }

    /**
     * 5. Reporte de citas por estado.
     */
    public ArrayList<Appointment> reportAppointmentsByState(String state) {
        if (state == null || state.isBlank() || state.equalsIgnoreCase("Todos")) {
            return reportAppointmentsComplete();
        }
        AppointmentsDto dto = new AppointmentsDto();
        return dto.readAppointmentsByState(state);
    }

    /**
     * 6. Reporte de cantidad de citas por especialidad.
     * Retorna filas: [Especialidad, Total Citas, Citas Programadas, Citas Atendidas, Citas Canceladas]
     */
    public ArrayList<Object[]> reportAppointmentsCountBySpeciality() {
        AppointmentsDto appointmentsDto = new AppointmentsDto();
        DoctorsDto doctorsDto = new DoctorsDto();

        ArrayList<Appointment> appointments = appointmentsDto.readAll();
        if (appointments == null) appointments = new ArrayList<>();

        // Obtener especialidad por médico
        ArrayList<Doctor> doctors = doctorsDto.readAll();
        Map<String, String> doctorSpecMap = new HashMap<>();
        Set<String> allSpecialities = new HashSet<>();

        if (doctors != null) {
            for (Doctor d : doctors) {
                String spec = (d.getSpeciality() != null && !d.getSpeciality().isBlank()) ? d.getSpeciality().trim() : "General";
                doctorSpecMap.put(d.getId(), spec);
                allSpecialities.add(spec);
            }
        }

        // [total, programadas, atendidas, canceladas]
        Map<String, int[]> specialityStats = new HashMap<>();
        for (String spec : allSpecialities) {
            specialityStats.put(spec, new int[4]);
        }

        for (Appointment a : appointments) {
            String spec = doctorSpecMap.getOrDefault(a.getIdDoctor(), "Sin Especialidad");
            specialityStats.putIfAbsent(spec, new int[4]);
            int[] s = specialityStats.get(spec);
            s[0]++; // Total
            if (AppointmentsDto.STATE_PROGRAMADA.equalsIgnoreCase(a.getState())) s[1]++;
            else if (AppointmentsDto.STATE_ATENDIDA.equalsIgnoreCase(a.getState())) s[2]++;
            else if (AppointmentsDto.STATE_CANCELADA.equalsIgnoreCase(a.getState())) s[3]++;
        }

        ArrayList<Object[]> result = new ArrayList<>();
        for (Map.Entry<String, int[]> entry : specialityStats.entrySet()) {
            int[] s = entry.getValue();
            result.add(new Object[]{
                entry.getKey(),
                s[0],
                s[1],
                s[2],
                s[3]
            });
        }

        // Orden descendente por total de citas
        result.sort((a, b) -> Integer.compare((int) b[1], (int) a[1]));
        return result;
    }
}

