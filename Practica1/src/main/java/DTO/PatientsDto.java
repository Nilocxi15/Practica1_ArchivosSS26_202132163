package DTO;

import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.ArrayList;
import models.Patient;

public class PatientsDto {

    public boolean writeRegister(Patient patient) {
        // Verificación de id's en registros
        try {
            RandomAccessFile raf = new RandomAccessFile("data/patients.dat", "rw");
            String tempId;
            // Declaración de tamaños específicos de campos
            final int idSize = 13;
            final int fieldsSize = 228;
            final int registerSize = idSize + fieldsSize + 1;

            long totalRegisters = raf.length() / registerSize;

            for (int i = 0; i < totalRegisters; i++) {
                byte[] idData = new byte[idSize];
                byte[] fieldsData = new byte[fieldsSize];

                raf.readFully(idData);
                raf.readFully(fieldsData);
                raf.readByte();                

                tempId = new String(idData).trim();

                if (tempId.equals(patient.getID())) {
                    raf.close();
                    return false;
                }
            }
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verficación de longitudes de campos
        patient.setName(String.format("%-50s", patient.getName()));
        patient.setLastname(String.format("%-50s", patient.getLastname()));
        patient.setCellphone(String.format("%-14s", patient.getCellphone()));
        patient.setEmail(String.format("%-100s", patient.getEmail()));
        patient.setBloodType(String.format("%-3s", patient.getBloodType()));

        // Escritura de registro nuevo
        try {
            RandomAccessFile raf = new RandomAccessFile("data/patients.dat", "rw");
            String tempId;
            // Declaración de tamaños específicos de campos
            final int idSize = 13;
            final int fieldsSize = 228;
            final int registerSize = idSize + fieldsSize + 1;

            long totalRegisters = raf.length() / registerSize;

            String idFormatted = String.format("%-13s", patient.getID());
            String birthdateFormatted = String.format("%-10s", patient.getBirthdate().toString());
            String genderFormatted = String.format("%-1s", patient.getGender());

            for (int i = 0; i < totalRegisters; i++) {
                byte[] idData = new byte[idSize];
                byte[] fieldsData = new byte[fieldsSize];

                raf.readFully(idData);
                raf.readFully(fieldsData);
                raf.readByte();                

                tempId = new String(idData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();

                // Si encuentra un registro "eliminado" escribe datos en esa posición                
                if (tempId.isBlank()) {
                    long position = raf.getFilePointer() - registerSize;
                    raf.seek(position);
                    // Escritura de datos
                    raf.writeBytes(idFormatted);
                    raf.writeBytes(patient.getName());
                    raf.writeBytes(patient.getLastname());
                    raf.writeBytes(birthdateFormatted);
                    raf.writeBytes(genderFormatted);
                    raf.writeBytes(patient.getCellphone());
                    raf.writeBytes(patient.getEmail());
                    raf.writeBytes(patient.getBloodType());
                    raf.writeBytes("\n");
                    raf.close();
                    return true;
                }
            }
            // En caso de no encontrar coincidencia, escribe al final del archivo
            raf.seek(raf.length());
            // Escritura de datos
            raf.writeBytes(idFormatted);
            raf.writeBytes(patient.getName());
            raf.writeBytes(patient.getLastname());
            raf.writeBytes(birthdateFormatted);
            raf.writeBytes(genderFormatted);
            raf.writeBytes(patient.getCellphone());
            raf.writeBytes(patient.getEmail());
            raf.writeBytes(patient.getBloodType());
            raf.writeBytes("\n");
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public ArrayList<Patient> readAll() {
        try {
            // Declaración de tamaños específicos de cada campo
            final int idSize = 13;
            final int nameSize = 50;
            final int lastnameSize = 50;
            final int birthdateSize = 10;
            final int genderSize = 1;
            final int cellphoneSize = 14;
            final int emailSize = 100;
            final int bloodTypeSize = 3;

            final int registerSize = idSize + nameSize + lastnameSize
                    + birthdateSize + genderSize + cellphoneSize + emailSize
                    + bloodTypeSize + 1;
            /*
             * Arreglo de objetos tipo Patient
             */
            Patient patient;
            ArrayList<Patient> patients = new ArrayList<>();

            java.io.File file = new java.io.File("data/patients.dat");
            if (!file.exists()) {
                return patients;
            }

            RandomAccessFile raf = new RandomAccessFile("data/patients.dat", "r");

            while (raf.getFilePointer() + 241 <= raf.length()) {
                byte[] idData = new byte[idSize];
                byte[] nameData = new byte[nameSize];
                byte[] lastnameData = new byte[lastnameSize];
                byte[] birthdateData = new byte[birthdateSize];
                byte[] genderData = new byte[genderSize];
                byte[] cellphoneData = new byte[cellphoneSize];
                byte[] emailData = new byte[emailSize];
                byte[] bloodTypeData = new byte[bloodTypeSize];

                // Lectura de campos junto con movimiento de cursor
                raf.readFully(idData);
                raf.readFully(nameData);
                raf.readFully(lastnameData);
                raf.readFully(birthdateData);
                raf.readFully(genderData);
                raf.readFully(cellphoneData);
                raf.readFully(emailData);
                raf.readFully(bloodTypeData);

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

                // Parseo de datos binarios a String
                String id = new String(idData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String name = new String(nameData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String lastname = new String(lastnameData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String birthdateStr = new String(birthdateData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String gender = new String(genderData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String cellphone = new String(cellphoneData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String email = new String(emailData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
                String bloodType = new String(bloodTypeData, java.nio.charset.StandardCharsets.ISO_8859_1).trim();

                // Verificación de espacios en blanco (se ignoran)
                if (id.isBlank()) {
                    continue;
                }

                // Parseo de String a LocalDate
                LocalDate birthdate = LocalDate.parse(birthdateStr);

                // Llenado de ArrayList
                patient = new Patient(id, name, lastname, birthdate, gender, cellphone, email, bloodType);
                patients.add(patient);
            }
            raf.close();

            return patients;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<Patient> searchData(String data, int attribute) {
        ArrayList<Patient> patients = readAll();
        if (patients == null) {
            return new ArrayList<>();
        }
        ArrayList<Patient> patientsFiltered = new ArrayList<>();

        if (data == null || data.isBlank()) {
            return patients;
        }

        switch (attribute) {
            case 0: // Ninguno
                return patients;
            case 1: // No. Identificación                                
                for (Patient patient : patients) {
                    if (patient.getID().contains(data)) {
                        patientsFiltered.add(patient);
                    }
                }
                break;
            case 2: // Nombre  
                for (Patient patient : patients) {
                    if (patient.getName().contains(data)) {
                        patientsFiltered.add(patient);
                    }
                }
                break;
            case 3: // Apellido
                for (Patient patient : patients) {
                    if (patient.getLastname().contains(data)) {
                        patientsFiltered.add(patient);
                    }
                }
                break;
            default:
                patientsFiltered = patients;
                break;
        }
        return patientsFiltered;
    }

    public Patient searchRegister(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        ArrayList<Patient> patients = readAll();
        if (patients == null) {
            return null;
        }

        for (Patient patient : patients) {
            if (patient.getID().equals(id.trim())) {
                return patient;
            }
        }

        return null;
    }

    public boolean deleteRegister(String id) {
        String deletedId = " ".repeat(13);
        if (id.equals(deletedId)) {
            return false;
        }

        try {
            // Declaración de tamaños específicos de cada campo
            final int idSize = 13;
            final int nameSize = 50;
            final int lastnameSize = 50;
            final int birthdateSize = 10;
            final int genderSize = 1;
            final int cellphoneSize = 14;
            final int emailSize = 100;
            final int bloodTypeSize = 3;

            final int registerSize = idSize + nameSize + lastnameSize
                    + birthdateSize + genderSize + cellphoneSize + emailSize
                    + bloodTypeSize + 1;

            final int fieldsSize = registerSize - idSize - 1;

            RandomAccessFile raf = new RandomAccessFile("data/patients.dat", "rw");

            long totalRegisters = raf.length() / registerSize;

            String tempId = null;

            // Búsqueda de campo con el ID del paciente
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
                    // Eliminación de datos
                    raf.writeBytes(" ".repeat(idSize));
                    raf.writeBytes(" ".repeat(nameSize));
                    raf.writeBytes(" ".repeat(lastnameSize));
                    raf.writeBytes(" ".repeat(birthdateSize));
                    raf.writeBytes(" ".repeat(genderSize));
                    raf.writeBytes(" ".repeat(cellphoneSize));
                    raf.writeBytes(" ".repeat(emailSize));
                    raf.writeBytes(" ".repeat(bloodTypeSize));
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

    public boolean updatePatient(Patient p) {
        String deletedId = " ".repeat(13);
        if (p.getID().equals(deletedId)) {
            return false;
        }

        try {
            // Declaración de tamaños específicos de cada campo
            final int idSize = 13;
            final int nameSize = 50;
            final int lastnameSize = 50;
            final int birthdateSize = 10;
            final int genderSize = 1;
            final int cellphoneSize = 14;
            final int emailSize = 100;
            final int bloodTypeSize = 3;

            final int registerSize = idSize + nameSize + lastnameSize
                    + birthdateSize + genderSize + cellphoneSize + emailSize
                    + bloodTypeSize + 1;

            final int fieldsSize = registerSize - idSize - 1;

            RandomAccessFile raf = new RandomAccessFile("data/patients.dat", "rw");

            long totalRegisters = raf.length() / registerSize;

            String tempId = null;

            // Búsqueda de campo con el ID del paciente
            for (int i = 0; i < totalRegisters; i++) {
                byte[] idData = new byte[idSize];
                byte[] fieldsData = new byte[fieldsSize];

                raf.readFully(idData);
                raf.readFully(fieldsData);
                raf.readByte();

                tempId = new String(idData).trim();

                if (tempId.equals(p.getID())) {
                    long position = raf.getFilePointer();
                    position = position - fieldsSize - 1; // Coloca el puntero después del ID del registro
                    raf.seek(position);
                    // Actualización de datos del registro                    
                    raf.writeBytes(String.format("%-50s", p.getName()));
                    raf.writeBytes(String.format("%-50s", p.getLastname()));
                    raf.writeBytes(p.getBirthdate().toString());
                    raf.writeBytes(p.getGender());
                    raf.writeBytes(String.format("%-14s", p.getCellphone()));
                    raf.writeBytes(String.format("%-100s", p.getEmail()));
                    raf.writeBytes(String.format("%-3s", p.getBloodType()));
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
