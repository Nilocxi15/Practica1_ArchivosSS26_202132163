package util;

import java.io.RandomAccessFile;
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
                if (tempId.equals("NULL")) {
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
}
