package com.mycompany.practica1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.UUID;

class DataRolesGenerator {

    public void generateFile() {
        System.out.println("Iniciando generacion de archivo con datos de roles de inicio de sesion.");

        try {
            // Comprobación y/o creación de carpeta si existe
            this.createFolder();
            
            // Comprobación de si existe el archivo

            Path file = Path.of("data/roles.dat");

            if (Files.exists(file)) {
                System.out.println("El archivo ya existe. No se generó ningun archivo extra.");
                return;
            }

            // NOTA: una variable UUID genera un string de longitud 36, es decir 36 bytes
            // Banco de nombres y apellidos aleatorios
            final String[] NAMES = {
                "Juan",
                "Carlos",
                "Pedro",
                "Luis",
                "Miguel",
                "Jose",
                "Ana",
                "Maria",
                "Laura",
                "Sofia"
            };

            final String[] LASTNAMES = {
                "Garcia",
                "Lopez",
                "Perez",
                "Hernandez",
                "Gonzalez",
                "Ramirez",
                "Martinez",
                "Castillo",
                "Morales",
                "Cruz"
            };

            final Random random = new Random();
            int password = 10;
            RandomAccessFile raf = new RandomAccessFile("data/roles.dat", "rw");

            // Iteración con datos para llenar tuplas de archivo
            for (int i = 0; i < 11; i++) {
                // Generación de datos faltantes
                UUID id = UUID.randomUUID();
                String name = NAMES[random.nextInt(NAMES.length)];
                String lastname = LASTNAMES[random.nextInt(LASTNAMES.length)];
                String fullname = name + " " + lastname;
                String result = String.format("%-16s", fullname);
                password = password + 1;

                // Escritura de datos en archivo binario
                raf.seek(raf.length());
                raf.writeBytes(id.toString());
                raf.writeBytes(result);
                raf.writeBytes(String.valueOf(password));
                raf.writeBytes("\n");
            }

            raf.close();
            System.out.println("Archivo binario generado exitosamente.");
        } catch (Exception e) {
            System.out.println("ERROR. No fue posible generar el archivo binario");
        }
    }

    // Creación de carpeta para almacenar archivos binarios
    private void createFolder() throws IOException {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }
}
