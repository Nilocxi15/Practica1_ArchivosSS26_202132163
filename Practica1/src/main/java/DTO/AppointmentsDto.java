package DTO;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import models.Appointment;
import models.Doctor;
import models.Patient;

public class AppointmentsDto {

    public static final String STATE_PROGRAMADA = "Programada";
    public static final String STATE_ATENDIDA = "Atendida";
    public static final String STATE_CANCELADA = "Cancelada";

    private final String filePath = "data/appointments.dat";
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

    // Tamaños de campos en bytes
    private final int idSize = 36;
    private final int idPatientSize = 13;
    private final int idDoctorSize = 36;
    private final int dateSize = 10;
    private final int hourSize = 8;
    private final int reasonSize = 150;
    private final int stateSize = 15;
    private final int observationsSize = 150;

    private final int fieldsSize = idPatientSize + idDoctorSize + dateSize + hourSize
            + reasonSize + stateSize + observationsSize; // 382 bytes
    private final int registerSize = idSize + fieldsSize + 1; // 419 bytes

    /**
     * Programa una nueva cita en el archivo binario.
     * Si no se proporciona un UUID, se genera automáticamente.
     */
    public boolean writeRegister(Appointment appointment) {
        // Asegurar que la carpeta data exista
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Generación automática de UUID si no viene especificado
        String id = appointment.getIdAppointment();
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }

        // Estado por defecto
        String state = appointment.getState();
        if (state == null || state.isBlank()) {
            state = STATE_PROGRAMADA;
        }

        // Verificación de existencia de ID duplicado en el archivo
        try {
            RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
            long totalRegisters = raf.length() / registerSize;

            for (int i = 0; i < totalRegisters; i++) {
                byte[] idData = new byte[idSize];
                byte[] fData = new byte[fieldsSize];

                raf.readFully(idData);
                raf.readFully(fData);
                raf.readByte();

                String tempId = new String(idData, StandardCharsets.ISO_8859_1).trim();
                if (tempId.equals(id)) {
                    raf.close();
                    return false;
                }
            }
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Formateo de longitudes fijas de campos
        String idFormatted = String.format("%-36s", id);
        String patientFormatted = String.format("%-13s", appointment.getIdPatient() != null ? appointment.getIdPatient() : "");
        String doctorFormatted = String.format("%-36s", appointment.getIdDoctor() != null ? appointment.getIdDoctor() : "");
        String dateFormatted = String.format("%-10s", appointment.getDate() != null ? appointment.getDate().toString() : "");
        String hourFormatted = String.format("%-8s", appointment.getHour() != null ? appointment.getHour().format(timeFormatter) : "");
        String reasonFormatted = String.format("%-150s", appointment.getConsultationReason() != null ? appointment.getConsultationReason() : "");
        String stateFormatted = String.format("%-15s", state);
        String obsFormatted = String.format("%-150s", appointment.getObservations() != null ? appointment.getObservations() : "");

        try {
            RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
            long totalRegisters = raf.length() / registerSize;

            // Búsqueda de un registro previamente eliminado para reutilizar el espacio
            for (int i = 0; i < totalRegisters; i++) {
                byte[] idData = new byte[idSize];
                byte[] fData = new byte[fieldsSize];

                raf.readFully(idData);
                raf.readFully(fData);
                raf.readByte();

                String tempId = new String(idData, StandardCharsets.ISO_8859_1).trim();

                if (tempId.isBlank()) {
                    long position = raf.getFilePointer() - registerSize;
                    raf.seek(position);

                    raf.writeBytes(idFormatted);
                    raf.writeBytes(patientFormatted);
                    raf.writeBytes(doctorFormatted);
                    raf.writeBytes(dateFormatted);
                    raf.writeBytes(hourFormatted);
                    raf.writeBytes(reasonFormatted);
                    raf.writeBytes(stateFormatted);
                    raf.writeBytes(obsFormatted);
                    raf.writeBytes("\n");
                    raf.close();
                    return true;
                }
            }

            // Si no hay huecos disponibles, se escribe al final del archivo
            raf.seek(raf.length());
            raf.writeBytes(idFormatted);
            raf.writeBytes(patientFormatted);
            raf.writeBytes(doctorFormatted);
            raf.writeBytes(dateFormatted);
            raf.writeBytes(hourFormatted);
            raf.writeBytes(reasonFormatted);
            raf.writeBytes(stateFormatted);
            raf.writeBytes(obsFormatted);
            raf.writeBytes("\n");
            raf.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Consulta el listado completo de citas registradas (omitiendo eliminadas).
     */
    public ArrayList<Appointment> readAll() {
        ArrayList<Appointment> appointments = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return appointments;
        }

        try {
            RandomAccessFile raf = new RandomAccessFile(filePath, "r");
            while (raf.getFilePointer() + 418 <= raf.length()) {
                byte[] idData = new byte[idSize];
                byte[] idPatientData = new byte[idPatientSize];
                byte[] idDoctorData = new byte[idDoctorSize];
                byte[] dateData = new byte[dateSize];
                byte[] hourData = new byte[hourSize];
                byte[] reasonData = new byte[reasonSize];
                byte[] stateData = new byte[stateSize];
                byte[] observationsData = new byte[observationsSize];

                raf.readFully(idData);
                raf.readFully(idPatientData);
                raf.readFully(idDoctorData);
                raf.readFully(dateData);
                raf.readFully(hourData);
                raf.readFully(reasonData);
                raf.readFully(stateData);
                raf.readFully(observationsData);

                // Consumir salto de línea (\n o \r\n)
                if (raf.getFilePointer() < raf.length()) {
                    byte b = raf.readByte();
                    if (b == '\r' && raf.getFilePointer() < raf.length()) {
                        long mark = raf.getFilePointer();
                        if (raf.readByte() != '\n') {
                            raf.seek(mark);
                        }
                    }
                }

                String id = new String(idData, StandardCharsets.ISO_8859_1).trim();
                String idPatient = new String(idPatientData, StandardCharsets.ISO_8859_1).trim();
                String idDoctor = new String(idDoctorData, StandardCharsets.ISO_8859_1).trim();
                String dateStr = new String(dateData, StandardCharsets.ISO_8859_1).trim();
                String hourStr = new String(hourData, StandardCharsets.ISO_8859_1).trim();
                String reason = new String(reasonData, StandardCharsets.ISO_8859_1).trim();
                String state = new String(stateData, StandardCharsets.ISO_8859_1).trim();
                String observations = new String(observationsData, StandardCharsets.ISO_8859_1).trim();

                // Ignorar registros eliminados
                if (id.isBlank()) {
                    continue;
                }

                LocalDate date = LocalDate.parse(dateStr);
                LocalTime hour = DoctorsDto.parseTimeFlexible(hourStr);

                Appointment appointment = new Appointment(id, idPatient, idDoctor, date, hour, reason, state, observations);
                appointments.add(appointment);
            }
            raf.close();
            return appointments;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Busca una cita específica por su identificador UUID.
     */
    public Appointment searchRegister(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        ArrayList<Appointment> list = readAll();
        for (Appointment a : list) {
            if (a.getIdAppointment().equals(id.trim())) {
                return a;
            }
        }
        return null;
    }

    /**
     * Consulta todas las citas asociadas a un paciente específico.
     */
    public ArrayList<Appointment> readAppointmentsByPatient(String idPatient) {
        ArrayList<Appointment> filtered = new ArrayList<>();
        if (idPatient == null || idPatient.isBlank()) {
            return filtered;
        }
        for (Appointment a : readAll()) {
            if (a.getIdPatient().equals(idPatient.trim())) {
                filtered.add(a);
            }
        }
        return filtered;
    }

    /**
     * Consulta todas las citas asociadas a un médico específico.
     */
    public ArrayList<Appointment> readAppointmentsByDoctor(String idDoctor) {
        ArrayList<Appointment> filtered = new ArrayList<>();
        if (idDoctor == null || idDoctor.isBlank()) {
            return filtered;
        }
        for (Appointment a : readAll()) {
            if (a.getIdDoctor().equals(idDoctor.trim())) {
                filtered.add(a);
            }
        }
        return filtered;
    }

    /**
     * Consulta todas las citas programadas para una fecha específica.
     */
    public ArrayList<Appointment> readAppointmentsByDate(LocalDate date) {
        ArrayList<Appointment> filtered = new ArrayList<>();
        if (date == null) {
            return filtered;
        }
        for (Appointment a : readAll()) {
            if (a.getDate().equals(date)) {
                filtered.add(a);
            }
        }
        return filtered;
    }

    /**
     * Consulta todas las citas según su estado (Programada, Atendida, Cancelada).
     */
    public ArrayList<Appointment> readAppointmentsByState(String state) {
        ArrayList<Appointment> filtered = new ArrayList<>();
        if (state == null || state.isBlank()) {
            return filtered;
        }
        for (Appointment a : readAll()) {
            if (a.getState().equalsIgnoreCase(state.trim())) {
                filtered.add(a);
            }
        }
        return filtered;
    }

    /**
     * Permite realizar búsquedas combinadas de citas por atributo y estado.
     * @param data Texto a buscar
     * @param attribute 0: Todo, 1: ID, 2: Paciente, 3: Médico, 4: Fecha
     * @param stateFilter 0: Todo, 1: Programada, 2: Atendida, 3: Cancelada
     */
    public ArrayList<Appointment> searchData(String data, int attribute, int stateFilter) {
        ArrayList<Appointment> appointments;

        switch (stateFilter) {
            case 0 -> appointments = readAll();
            case 1 -> appointments = readAppointmentsByState(STATE_PROGRAMADA);
            case 2 -> appointments = readAppointmentsByState(STATE_ATENDIDA);
            case 3 -> appointments = readAppointmentsByState(STATE_CANCELADA);
            default -> appointments = readAll();
        }

        if (data == null || data.isBlank()) {
            return appointments;
        }

        String search = data.trim().toLowerCase();
        ArrayList<Appointment> filtered = new ArrayList<>();
        PatientsDto patientsDto = new PatientsDto();
        DoctorsDto doctorsDto = new DoctorsDto();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        switch (attribute) {
            case 0: // Todo (Búsqueda en todos los campos según el estado filtrado)
                for (Appointment a : appointments) {
                    if (a.getIdAppointment().toLowerCase().contains(search)) {
                        filtered.add(a);
                        continue;
                    }
                    if (a.getIdPatient().toLowerCase().contains(search)) {
                        filtered.add(a);
                        continue;
                    }
                    Patient p = patientsDto.searchRegister(a.getIdPatient());
                    if (p != null) {
                        String fullName = (p.getName() + " " + p.getLastname()).toLowerCase();
                        if (fullName.contains(search)) {
                            filtered.add(a);
                            continue;
                        }
                    }
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
                            continue;
                        }
                    }
                    if (a.getDate().toString().contains(search) || a.getDate().format(dtf).contains(search)) {
                        filtered.add(a);
                        continue;
                    }
                    if (a.getConsultationReason().toLowerCase().contains(search)
                            || a.getObservations().toLowerCase().contains(search)) {
                        filtered.add(a);
                    }
                }
                break;
            case 1: // ID
                for (Appointment a : appointments) {
                    if (a.getIdAppointment().toLowerCase().contains(search)) {
                        filtered.add(a);
                    }
                }
                break;
            case 2: // Paciente
                for (Appointment a : appointments) {
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
                break;
            case 3: // Médico
                for (Appointment a : appointments) {
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
                break;
            case 4: // Fecha
                for (Appointment a : appointments) {
                    if (a.getDate().toString().contains(search) || a.getDate().format(dtf).contains(search)) {
                        filtered.add(a);
                    }
                }
                break;
            default:
                filtered = appointments;
                break;
        }

        return filtered;
    }

    /**
     * Actualiza todos los campos de una cita existente en el archivo binario.
     */
    public boolean updateAppointment(Appointment a) {
        if (a == null || a.getIdAppointment() == null || a.getIdAppointment().isBlank()) {
            return false;
        }

        try {
            RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
            long totalRegisters = raf.length() / registerSize;

            for (int i = 0; i < totalRegisters; i++) {
                byte[] idData = new byte[idSize];
                byte[] fData = new byte[fieldsSize];

                raf.readFully(idData);
                raf.readFully(fData);
                raf.readByte();

                String tempId = new String(idData, StandardCharsets.ISO_8859_1).trim();

                if (tempId.equals(a.getIdAppointment().trim())) {
                    long position = raf.getFilePointer() - fieldsSize - 1;
                    raf.seek(position);

                    String patientFormatted = String.format("%-13s", a.getIdPatient() != null ? a.getIdPatient() : "");
                    String doctorFormatted = String.format("%-36s", a.getIdDoctor() != null ? a.getIdDoctor() : "");
                    String dateFormatted = String.format("%-10s", a.getDate() != null ? a.getDate().toString() : "");
                    String hourFormatted = String.format("%-8s", a.getHour() != null ? a.getHour().format(timeFormatter) : "");
                    String reasonFormatted = String.format("%-150s", a.getConsultationReason() != null ? a.getConsultationReason() : "");
                    String stateFormatted = String.format("%-15s", a.getState() != null ? a.getState() : STATE_PROGRAMADA);
                    String obsFormatted = String.format("%-150s", a.getObservations() != null ? a.getObservations() : "");

                    raf.writeBytes(patientFormatted);
                    raf.writeBytes(doctorFormatted);
                    raf.writeBytes(dateFormatted);
                    raf.writeBytes(hourFormatted);
                    raf.writeBytes(reasonFormatted);
                    raf.writeBytes(stateFormatted);
                    raf.writeBytes(obsFormatted);
                    raf.writeBytes("\n");
                    raf.close();
                    return true;
                }
            }
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cancela una cita cambiando su estado a "Cancelada".
     */
    public boolean cancelAppointment(String id) {
        Appointment a = searchRegister(id);
        if (a != null) {
            a.setState(STATE_CANCELADA);
            return updateAppointment(a);
        }
        return false;
    }

    /**
     * Marca una cita como "Atendida".
     */
    public boolean attendAppointment(String id) {
        Appointment a = searchRegister(id);
        if (a != null) {
            a.setState(STATE_ATENDIDA);
            return updateAppointment(a);
        }
        return false;
    }

    /**
     * Modifica el motivo de consulta y/u observaciones de una cita.
     */
    public boolean updateAppointmentDetails(String id, String newReason, String newObservations) {
        Appointment a = searchRegister(id);
        if (a != null) {
            if (newReason != null) {
                a.setConsultationReason(newReason);
            }
            if (newObservations != null) {
                a.setObservations(newObservations);
            }
            return updateAppointment(a);
        }
        return false;
    }

    /**
     * Permite reprogramar la fecha y hora de una cita.
     */
    public boolean rescheduleAppointment(String id, LocalDate newDate, LocalTime newHour) {
        Appointment a = searchRegister(id);
        if (a != null) {
            if (newDate != null) {
                a.setDate(newDate);
            }
            if (newHour != null) {
                a.setHour(newHour);
            }
            return updateAppointment(a);
        }
        return false;
    }

    /**
     * Elimina físicamente una cita llenando el registro con espacios en blanco.
     */
    public boolean deleteRegister(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }

        try {
            RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
            long totalRegisters = raf.length() / registerSize;

            for (int i = 0; i < totalRegisters; i++) {
                byte[] idData = new byte[idSize];
                byte[] fData = new byte[fieldsSize];

                raf.readFully(idData);
                raf.readFully(fData);
                raf.readByte();

                String tempId = new String(idData, StandardCharsets.ISO_8859_1).trim();

                if (tempId.equals(id.trim())) {
                    long position = raf.getFilePointer() - registerSize;
                    raf.seek(position);

                    raf.writeBytes(" ".repeat(idSize));
                    raf.writeBytes(" ".repeat(idPatientSize));
                    raf.writeBytes(" ".repeat(idDoctorSize));
                    raf.writeBytes(" ".repeat(dateSize));
                    raf.writeBytes(" ".repeat(hourSize));
                    raf.writeBytes(" ".repeat(reasonSize));
                    raf.writeBytes(" ".repeat(stateSize));
                    raf.writeBytes(" ".repeat(observationsSize));
                    raf.writeBytes("\n");
                    raf.close();
                    return true;
                }
            }
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
