package DTO;

import java.io.RandomAccessFile;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import models.Doctor;

public class DoctorsDto {

    public boolean writeRegister(Doctor doctor) {
        // Verificación de id's en registros
        try {
            RandomAccessFile raf = new RandomAccessFile("data/doctors.dat", "rw");
            String tempId;
            // Declaración de tamaños especificos de campos
            final int idSize = 36;
            final int fieldsSize = 281;
            final int registerSize = idSize + fieldsSize + 1;

            long totalRegisters = raf.length() / registerSize;

            for (int i = 0; i < totalRegisters; i++) {
                byte[] idData = new byte[idSize];
                byte[] fieldsData = new byte[fieldsSize];

                raf.readFully(idData);
                raf.readFully(fieldsData);
                raf.readByte();

                tempId = new String(idData).trim();

                if (tempId.equals(doctor.getId())) {
                    raf.close();
                    return false;
                }
            }
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verificación de longitudes de campos
        // Verificación de nombre (50 caracteres)
        doctor.setName(String.format("%-50s", doctor.getName()));
        // Verficiación de apellido (50 caracteres)
        doctor.setLastname(String.format("%-50s", doctor.getLastname()));
        // Especialidad (50 caracteres)
        doctor.setSpeciality(String.format("%-50s", doctor.getSpeciality()));
        // Celular (14 dígitos)
        doctor.setCellphone(String.format("%-14s", doctor.getCellphone()));
        // Email
        doctor.setEmail(String.format("%-100s", doctor.getEmail()));

        // Escritura de registro nuevo
        try {
            RandomAccessFile raf = new RandomAccessFile("data/doctors.dat", "rw");
            String tempId;
            // Declaracio´n de tamaños especificos de campos
            final int idSize = 36;
            final int fieldsSize = 281;
            final int registerSize = idSize + fieldsSize + 1;

            // Parseo de datos tipo LocalDate a String (horarios de médicos)
            DateTimeFormatter format = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
            String startHour = doctor.getStartShift().format(format);
            String endHour = doctor.getEndShift().format(format);

            // Parseo de boolean a String (estado del registro)
            doctor.setState(true);
            String stateStr = doctor.isState() ? "1" : "0";

            long totalRegisters = raf.length() / registerSize;

            for (int i = 0; i < totalRegisters; i++) {
                byte[] idData = new byte[idSize];
                byte[] fieldsData = new byte[fieldsSize];

                raf.readFully(idData);
                raf.readFully(fieldsData);
                raf.readByte();

                tempId = new String(idData).trim();

                // Si encuentra un registro "eliminado" escribe datos en esa posición
                if (tempId.isBlank()) {
                    long position = raf.getFilePointer();
                    position = position - registerSize;
                    raf.seek(position);
                    // Escritura de datos
                    raf.writeBytes(String.format("%-36s", doctor.getId()));
                    raf.writeBytes(doctor.getName());
                    raf.writeBytes(doctor.getLastname());
                    raf.writeBytes(doctor.getSpeciality());
                    raf.writeBytes(doctor.getCellphone());
                    raf.writeBytes(doctor.getEmail());
                    raf.writeBytes(startHour);
                    raf.writeBytes(endHour);
                    raf.writeBytes(stateStr);
                    raf.writeBytes("\n");
                    raf.close();
                    return true;
                }
            }

            // En caso de no encontrar coincidencias, escribe al final del archivo
            raf.seek(raf.length());
            // Escritura de datos
            raf.writeBytes(String.format("%-36s", doctor.getId()));
            raf.writeBytes(doctor.getName());
            raf.writeBytes(doctor.getLastname());
            raf.writeBytes(doctor.getSpeciality());
            raf.writeBytes(doctor.getCellphone());
            raf.writeBytes(doctor.getEmail());
            raf.writeBytes(startHour);
            raf.writeBytes(endHour);
            raf.writeBytes(stateStr);
            raf.writeBytes("\n");
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public ArrayList<Doctor> readAll() {
        try {
            // Declaración de tamaños específicos de cada campo
            final int idSize = 36;
            final int nameSize = 50;
            final int lastnameSize = 50;
            final int specialitySize = 50;
            final int cellphoneSize = 14;
            final int emailSize = 100;
            final int startShiftSize = 8;
            final int endShiftSize = 8;
            final int stateSize = 1;

            final int registerSize = idSize + nameSize + lastnameSize
                    + specialitySize + cellphoneSize + emailSize + startShiftSize
                    + endShiftSize + stateSize + 1;

            /*
             * Arreglo de objetos tipo Doctor
             */
            Doctor doctor;
            ArrayList<Doctor> doctors = new ArrayList<>();

            RandomAccessFile raf = new RandomAccessFile("data/doctors.dat", "r");

            long totalRegisters = raf.length() / registerSize;

            for (int i = 0; i < totalRegisters; i++) {
                byte[] idData = new byte[idSize];
                byte[] nameData = new byte[nameSize];
                byte[] lastnameData = new byte[lastnameSize];
                byte[] specialityData = new byte[specialitySize];
                byte[] cellphoneData = new byte[cellphoneSize];
                byte[] emailData = new byte[emailSize];
                byte[] startShiftData = new byte[startShiftSize];
                byte[] endShiftData = new byte[endShiftSize];
                byte[] stateData = new byte[stateSize];

                // Lectura de campos junto con movimiento de cursor
                raf.readFully(idData);
                raf.readFully(nameData);
                raf.readFully(lastnameData);
                raf.readFully(specialityData);
                raf.readFully(cellphoneData);
                raf.readFully(emailData);
                raf.readFully(startShiftData);
                raf.readFully(endShiftData);
                raf.readFully(stateData);
                raf.readByte(); // Salto de línea

                // Parseo de datos binarios a String
                String id = new String(idData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String name = new String(nameData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String lastname = new String(lastnameData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String speciality = new String(specialityData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String cellphone = new String(cellphoneData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String email = new String(emailData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String startShift = new String(startShiftData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String endShift = new String(endShiftData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String state = new String(stateData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();

                // Verificación de registros eliminados (se ignoran)
                if (id.isBlank()) {
                    continue;
                }

                // Parseo de String a LocalTime
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
                LocalTime startHour = LocalTime.parse(startShift, dtf);
                LocalTime endHour = LocalTime.parse(endShift, dtf);

                // Parse de String a boolean
                boolean stateBool = "1".equals(state);

                // Llenado de ArrayList
                doctor = new Doctor(id, name, lastname, speciality, cellphone, email, startHour, endHour, stateBool);
                doctors.add(doctor);
            }
            raf.close();

            return doctors;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<Doctor> readDoctorsActives() {
        // Obtención de registros de la función principal (readAll)
        ArrayList<Doctor> allDoctors = readAll();
        ArrayList<Doctor> activeDoctors = new ArrayList<>();

        if (allDoctors != null) {
            for (Doctor doctor : allDoctors) {
                if (doctor.isState()) {
                    activeDoctors.add(doctor);
                }
            }
        }

        return activeDoctors;
    }

    public ArrayList<Doctor> readDoctorsInactive() {
        // Obtención de registros de la función principal (readAll)
        ArrayList<Doctor> allDoctors = readAll();
        ArrayList<Doctor> activeDoctors = new ArrayList<>();

        if (allDoctors != null) {
            for (Doctor doctor : allDoctors) {
                if (!doctor.isState()) {
                    activeDoctors.add(doctor);
                }
            }
        }

        return activeDoctors;
    }

    public ArrayList<Doctor> searchData(String data, int attribute, int state) {
        ArrayList<Doctor> doctors;
        ArrayList<Doctor> doctorsFiltered = new ArrayList<>();

        switch (state) {
            case 0 -> doctors = readAll();
            case 1 -> doctors = readDoctorsActives();
            case 2 -> doctors = readDoctorsInactive();
            default -> throw new AssertionError();
        }

        // ID, nombre, apellido, especialidad
        switch (attribute) {
            case 0: // Ninguno
                return doctors;
            case 1:// ID
                for (Doctor doctor : doctors) {
                    if (doctor.getId().contains(data)) {
                        doctorsFiltered.add(doctor);
                    }
                }
                break;
            case 2: // Nombre
                for (Doctor doctor : doctors) {
                    if (doctor.getName().contains(data)) {
                        doctorsFiltered.add(doctor);
                    }
                }
                break;
            case 3: // Apellido
                for (Doctor doctor : doctors) {
                    if (doctor.getLastname().contains(data)) {
                        doctorsFiltered.add(doctor);
                    }
                }
                break;
            case 4: // Especialidad
                for (Doctor doctor : doctors) {
                    if (doctor.getSpeciality().contains(data)) {
                        doctorsFiltered.add(doctor);
                    }
                }
                break;
            default:
                doctorsFiltered = null;
                throw new AssertionError();
        }

        return doctorsFiltered;
    }

    public Doctor searchRegister(String id) {
        ArrayList<Doctor> doctors = readAll();

        Doctor d = null;

        for (Doctor doctor : doctors) {
            if (doctor.getId().equals(id)) {
                d = doctor;
            }
        }

        return d;
    }

    public boolean deleteRegister(String id) {
        String deletedId = " ".repeat(36);
        if (id.equals(deletedId)) {
            return false;
        }

        try {
            // Declaración de tamaños específicos de cada campo
            final int idSize = 36;
            final int nameSize = 50;
            final int lastnameSize = 50;
            final int specialitySize = 50;
            final int cellphoneSize = 14;
            final int emailSize = 100;
            final int startShiftSize = 8;
            final int endShiftSize = 8;
            final int stateSize = 1;

            final int registerSize = idSize + nameSize + lastnameSize
                    + specialitySize + cellphoneSize + emailSize + startShiftSize
                    + endShiftSize + stateSize + 1;

            final int fieldsSize = registerSize - idSize - 1;

            RandomAccessFile raf = new RandomAccessFile("data/doctors.dat", "rw");

            long totalRegisters = raf.length() / registerSize;

            String tempId = null;

            // Búsqueda de campo con el ID del doctor
            for (int i = 0; i < totalRegisters; i++) {
                byte[] idData = new byte[idSize];
                byte[] fieldsData = new byte[fieldsSize];

                raf.readFully(idData);
                raf.readFully(fieldsData);
                raf.readByte();

                tempId = new String(idData).trim();

                if (tempId.equals(id)) {
                    long position = raf.getFilePointer();
                    position = position - registerSize;
                    raf.seek(position);
                    // Eliminación de datos del registro encontrado
                    raf.writeBytes(" ".repeat(idSize));
                    raf.writeBytes(" ".repeat(nameSize));
                    raf.writeBytes(" ".repeat(lastnameSize));
                    raf.writeBytes(" ".repeat(specialitySize));
                    raf.writeBytes(" ".repeat(cellphoneSize));
                    raf.writeBytes(" ".repeat(emailSize));
                    raf.writeBytes(" ".repeat(startShiftSize));
                    raf.writeBytes(" ".repeat(endShiftSize));
                    raf.writeBytes(" ".repeat(stateSize));
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

    public boolean updateDoctor(Doctor d) {
        String deletedId = " ".repeat(36);
        if (d.getId().equals(deletedId)) {
            return false;
        }

        try {
            // Declaración de tamaños específicos de cada campo
            final int idSize = 36;
            final int nameSize = 50;
            final int lastnameSize = 50;
            final int specialitySize = 50;
            final int cellphoneSize = 14;
            final int emailSize = 100;
            final int startShiftSize = 8;
            final int endShiftSize = 8;
            final int stateSize = 1;

            final int registerSize = idSize + nameSize + lastnameSize
                    + specialitySize + cellphoneSize + emailSize + startShiftSize
                    + endShiftSize + stateSize + 1;

            final int fieldsSize = registerSize - idSize - 1;

            RandomAccessFile raf = new RandomAccessFile("data/doctors.dat", "rw");

            long totalRegisters = raf.length() / registerSize;

            String tempId = null;

            // Búsqueda de campo con el ID del doctor
            for (int i = 0; i < totalRegisters; i++) {
                byte[] idData = new byte[idSize];
                byte[] fieldsData = new byte[fieldsSize];

                raf.readFully(idData);
                raf.readFully(fieldsData);
                raf.readByte();

                tempId = new String(idData).trim();

                if (tempId.equals(d.getId())) {
                    long position = raf.getFilePointer();
                    position = position - fieldsSize - 1; // Coloca el puntero después del ID del registro
                    raf.seek(position);

                    // Parseo de datos tipo LocalDate a String (horarios de médicos)
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
                    String startHour = d.getStartShift().format(format);
                    String endHour = d.getEndShift().format(format);

                    // Parseo de boolean a String (estado del registro)
                    String stateStr = d.isState() ? "1" : "0";

                    // Actualización de datos dle registro
                    raf.writeBytes(String.format("%-50s", d.getName()));
                    raf.writeBytes(String.format("%-50s", d.getLastname()));
                    raf.writeBytes(String.format("%-50s", d.getSpeciality()));
                    raf.writeBytes(String.format("%-14s", d.getCellphone()));
                    raf.writeBytes(String.format("%-100s", d.getEmail()));
                    raf.writeBytes(startHour);
                    raf.writeBytes(endHour);
                    raf.writeBytes(stateStr);
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
