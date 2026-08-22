package util;

import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.ArrayList;
import models.Patient;

public class Patients {

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
                    return false;
                }
            }
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verficación de longitudes de campos
        // Verificación de nombre (50 caracteres)
        patient.setName(String.format("%-50s", patient.getName()));
        // Verificación de apellido (50 caracteres)
        patient.setLastname(String.format("%-50s", patient.getLastname()));
        // Verificación de número telefónico (14 digitos)
        patient.setCellphone(String.format("%-14s", patient.getCellphone()));
        // Verificación de email (limitado a 100 caracteres posibles)
        patient.setEmail(String.format("%-100s", patient.getEmail()));
        // Verificación de tipo de sangre (3 caracteres)
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

            for (int i = 0; i < totalRegisters; i++) {
                byte[] idData = new byte[idSize];
                byte[] fieldsData = new byte[fieldsSize];

                raf.readFully(idData);
                raf.readFully(fieldsData);
                raf.readByte();

                tempId = new String(idData).trim();

                // Si encuentra un registro "eliminado" escribe datos en esa posición
                String idEliminated = "".repeat(idSize);
                if (tempId.equals(idEliminated)) {
                    long position = raf.getFilePointer();
                    position = position - registerSize;
                    raf.seek(position);
                    // Escritura de datos
                    raf.writeBytes(patient.getID());
                    raf.writeBytes(patient.getName());
                    raf.writeBytes(patient.getLastname());
                    raf.writeBytes(patient.getBirthdate().toString());
                    raf.writeBytes(patient.getGender());
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
            raf.writeBytes(patient.getID());
            raf.writeBytes(patient.getName());
            raf.writeBytes(patient.getLastname());
            raf.writeBytes(patient.getBirthdate().toString());
            raf.writeBytes(patient.getGender());
            raf.writeBytes(patient.getCellphone());
            raf.writeBytes(patient.getEmail());
            raf.writeBytes(patient.getBloodType());
            raf.writeBytes("\n");
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
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
             * Arreglo de objetos
             */
            Patient patient;
            ArrayList<Patient> patients = new ArrayList<>();

            RandomAccessFile raf = new RandomAccessFile("data/patients.dat", "r");

            long totalRegisters = raf.length() / registerSize;

            for (int i = 0; i < totalRegisters; i++) {
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
                raf.readByte(); // Salto de línea

                // Parseo de datos binarios a String
                String id = new String(idData).trim();
                String name = new String(nameData).trim();
                String lastname = new String(lastnameData).trim();
                String birthdateStr = new String(birthdateData).trim();
                String gender = new String(genderData).trim();
                String cellphone = new String(cellphoneData).trim();
                String email = new String(emailData).trim();
                String bloodType = new String(bloodTypeData).trim();

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

            return patients;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Patient> searchData(String data, int attribute) {
        ArrayList<Patient> patients = readAll();
        ArrayList<Patient> patientsFiltered = new ArrayList<>();

        switch (attribute) {
            case 0: // Ninguno
                return null;
            case 1: // No. Identificación                                
                // Filtrado de búsqueda                
                for (Patient patient : patients) {
                    if (patient.getID().contains(data)) {
                        patientsFiltered.add(patient);
                    }
                }
                break;
            case 2: // Nombre  
                // Filtrado de búsqueda
                for (Patient patient : patients) {
                    if (patient.getName().contains(data)) {
                        patientsFiltered.add(patient);
                    }
                }
                break;
            case 3: // Apellido
                // Filtrado de búsqueda
                for (Patient patient : patients) {
                    if (patient.getLastname().contains(data)) {
                        patientsFiltered.add(patient);
                    }
                }
                break;
            default:
                patientsFiltered = null;
                throw new AssertionError();
        }
        return patientsFiltered;
    }

    public Patient searchRegister(String id) {
        ArrayList<Patient> patients = readAll();

        Patient p = null;

        for (Patient patient : patients) {
            if (patient.getID().equals(id)) {
                p = patient;
            }
        }

        return p;
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
