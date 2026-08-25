package com.mycompany.practica1;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DataTestGenerator {

    private static final String[] NAMES_M = {
        "Carlos", "Juan", "Pedro", "Luis", "Miguel", "Jose", "Alejandro", "Fernando", "Roberto",
        "Diego", "Manuel", "Ricardo", "Eduardo", "Francisco", "Andres", "Sergio", "Hector", "Javier"
    };

    private static final String[] NAMES_F = {
        "Ana", "Maria", "Laura", "Sofia", "Lucia", "Elena", "Carmen", "Isabel", "Patricia",
        "Gabriela", "Daniela", "Andrea", "Camila", "Valentina", "Paola", "Mariana", "Fernanda", "Claudia"
    };

    private static final String[] LASTNAMES = {
        "Garcia", "Lopez", "Perez", "Hernandez", "Gonzalez", "Ramirez", "Martinez", "Castillo",
        "Morales", "Cruz", "Gomez", "Reyes", "Alvarez", "Mendoza", "Vargas", "Ruiz", "Castaneda",
        "Rios", "Fuentes", "Aguilar", "Estrada", "Guzman", "Salazar", "Ramos", "Soto", "Herrera"
    };

    private static final String[] SPECIALITIES = {
        "Pediatria", "Cardiologia", "Neurologia", "Traumatologia", "Dermatologia",
        "Ginecologia", "Medicina General", "Oftalmologia", "Psiquiatria", "Oncologia",
        "Gastroenterologia", "Neumologia", "Nefrologia", "Endocrinologia"
    };

    private static final String[] BLOOD_TYPES = {
        "O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"
    };

    private static final String[] REASONS = {
        "Chequeo medico general y revision de signos vitales",
        "Dolor agudo en la zona lumbar y dificultad de movimiento",
        "Control mensual de hipertension y ajuste de medicacion",
        "Evaluacion por cuadro respiratorio persistente y tos",
        "Consulta de seguimiento post-operatorio y retiro de puntos",
        "Fiebre continua durante 3 dias acompanada de malestar general",
        "Molestias estomacales severas y digestion lenta",
        "Revision oftalmologica anual y cambio de graduacion",
        "Evaluacion cardiologica preventiva por antecedentes familiares",
        "Tratamiento de dermatitis por reaccion alergica",
        "Control prenatal y ultrasonido de rutina",
        "Dolor articular en rodillas y hombros",
        "Migrana frecuente con fotosensibilidad",
        "Revision de examenes de laboratorio de rutina",
        "Chequeo de niveles de glucosa y dieta diabetica"
    };

    private static final String[] OBSERVATIONS = {
        "Paciente estable, continuar con tratamiento indicado",
        "Se solicitan examenes de sangre y radiografia de control",
        "Reposo por 48 horas e hidratacion abundante recomendada",
        "Ajuste en la dosis de medicamentos segun tolerancia",
        "Programar proxima cita de control en 15 dias",
        "Se remite a especialista para valoracion complementaria",
        "Paciente reporta mejoria progresiva de sintomas",
        "Sin complicaciones aparentes, dar seguimiento ambulatorio",
        "Se prescribe analgesico y desinflamatorio por 5 dias",
        "Observacion preventiva, monitorear temperatura corporal"
    };

    public static void main(String[] args) {
        generateAllData();
    }

    public static void generateAllData() {
        System.out.println("--- GENERANDO DATASETS AMPLIADOS PARA PRUEBAS ---");
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();

        List<String> doctorIds = generateDoctors(25);
        List<String> patientIds = generatePatients(45);
        generateAppointments(80, doctorIds, patientIds);
        generateLogs(60, doctorIds, patientIds);
        System.out.println("--- GENERACION FINALIZADA CON EXITO ---");
    }

    public static List<String> generateDoctors(int count) {
        List<String> ids = new ArrayList<>();
        Random random = new Random(42); // Semilla fija para reproducibilidad
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", java.util.Locale.ENGLISH);

        try (RandomAccessFile raf = new RandomAccessFile("data/doctors.dat", "rw")) {
            raf.setLength(0); // Limpiar archivo previo

            for (int i = 0; i < count; i++) {
                String id = UUID.randomUUID().toString();
                ids.add(id);

                boolean isFemale = (i % 2 == 0);
                String name = isFemale ? NAMES_F[random.nextInt(NAMES_F.length)] : NAMES_M[random.nextInt(NAMES_M.length)];
                String lastname = LASTNAMES[random.nextInt(LASTNAMES.length)] + " " + LASTNAMES[random.nextInt(LASTNAMES.length)];
                String spec = SPECIALITIES[i % SPECIALITIES.length];
                String phone = "+502 " + (3000 + random.nextInt(5000)) + "-" + String.format("%04d", random.nextInt(10000));
                String email = name.toLowerCase() + "." + lastname.toLowerCase().replace(" ", "") + "@hospital.org";

                int startH = 7 + (i % 6); // 07:00 a 12:00
                int endH = startH + 6 + (i % 3); // 6 a 8 horas de turno
                if (endH > 22) endH = 22;

                LocalTime startShift = LocalTime.of(startH, 0);
                LocalTime endShift = LocalTime.of(endH, 0);
                String startShiftStr = startShift.format(timeFormatter);
                String endShiftStr = endShift.format(timeFormatter);

                // 90% activos, 10% inactivos
                String stateStr = (i % 10 == 9) ? "0" : "1";

                String line = String.format("%-36s", id)
                        + String.format("%-50s", name)
                        + String.format("%-50s", lastname)
                        + String.format("%-50s", spec)
                        + String.format("%-14s", phone)
                        + String.format("%-100s", email)
                        + String.format("%-8s", startShiftStr)
                        + String.format("%-8s", endShiftStr)
                        + String.format("%-1s", stateStr)
                        + "\n";

                raf.write(line.getBytes(StandardCharsets.ISO_8859_1));
            }
            System.out.println("Generados " + count + " medicos en data/doctors.dat (Tamanio: " + raf.length() + " bytes)");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    public static List<String> generatePatients(int count) {
        List<String> ids = new ArrayList<>();
        Random random = new Random(101);

        try (RandomAccessFile raf = new RandomAccessFile("data/patients.dat", "rw")) {
            raf.setLength(0);

            for (int i = 0; i < count; i++) {
                // DPI guatemalteco de 13 digitos
                long dpiNum = 2000000000000L + (long) (random.nextDouble() * 999999999999L);
                String id = String.valueOf(dpiNum).substring(0, 13);
                ids.add(id);

                boolean isFemale = (i % 2 == 1);
                String name = isFemale ? NAMES_F[random.nextInt(NAMES_F.length)] : NAMES_M[random.nextInt(NAMES_M.length)];
                String lastname = LASTNAMES[random.nextInt(LASTNAMES.length)] + " " + LASTNAMES[random.nextInt(LASTNAMES.length)];
                
                int birthYear = 1955 + random.nextInt(65);
                int birthMonth = 1 + random.nextInt(12);
                int birthDay = 1 + random.nextInt(28);
                LocalDate birthDate = LocalDate.of(birthYear, birthMonth, birthDay);

                String gender = isFemale ? "F" : "M";
                String phone = "+502 " + (4000 + random.nextInt(5000)) + "-" + String.format("%04d", random.nextInt(10000));
                String email = name.toLowerCase() + "." + lastname.toLowerCase().replace(" ", "") + "@gmail.com";
                String bloodType = BLOOD_TYPES[i % BLOOD_TYPES.length];

                String line = String.format("%-13s", id)
                        + String.format("%-50s", name)
                        + String.format("%-50s", lastname)
                        + String.format("%-10s", birthDate.toString())
                        + String.format("%-1s", gender)
                        + String.format("%-14s", phone)
                        + String.format("%-100s", email)
                        + String.format("%-3s", bloodType)
                        + "\n";

                raf.write(line.getBytes(StandardCharsets.ISO_8859_1));
            }
            System.out.println("Generados " + count + " pacientes en data/patients.dat (Tamanio: " + raf.length() + " bytes)");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    public static void generateAppointments(int count, List<String> doctorIds, List<String> patientIds) {
        Random random = new Random(777);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", java.util.Locale.ENGLISH);

        // Dejamos los ultimos 10 pacientes sin citas para probar el reporte de "pacientes sin citas"
        int activePatientsCount = patientIds.size() - 10;

        String[] states = {"Programada", "Atendida", "Cancelada"};

        try (RandomAccessFile raf = new RandomAccessFile("data/appointments.dat", "rw")) {
            raf.setLength(0);

            for (int i = 0; i < count; i++) {
                String id = UUID.randomUUID().toString();
                String idDoctor = doctorIds.get(random.nextInt(doctorIds.size()));
                
                // Distribucion sesgada: los primeros pacientes tendran mas citas
                int patientIdx = (i < 30) ? (i % 5) : random.nextInt(activePatientsCount);
                String idPatient = patientIds.get(patientIdx);

                // Fechas: algunas en el pasado (atendidas/canceladas), algunas hoy y otras futuras (programadas)
                LocalDate date;
                String state;
                if (i < 35) {
                    // Citas pasadas atendidas
                    date = LocalDate.now().minusDays(1 + random.nextInt(60));
                    state = (i % 6 == 0) ? "Cancelada" : "Atendida";
                } else if (i < 50) {
                    // Citas para hoy / manana
                    date = (i % 2 == 0) ? LocalDate.now() : LocalDate.now().plusDays(1);
                    state = (i % 5 == 0) ? "Cancelada" : "Programada";
                } else {
                    // Citas futuras
                    date = LocalDate.now().plusDays(2 + random.nextInt(30));
                    state = (i % 8 == 0) ? "Cancelada" : "Programada";
                }

                int hour = 8 + (i % 9); // 08:00 a 16:00
                int minute = (i % 2 == 0) ? 0 : 30;
                LocalTime time = LocalTime.of(hour, minute);
                String hourStr = time.format(timeFormatter);

                String reason = REASONS[i % REASONS.length];
                String obs = (state.equals("Programada")) ? "Cita pendiente de atencion" : OBSERVATIONS[i % OBSERVATIONS.length];

                String line = String.format("%-36s", id)
                        + String.format("%-13s", idPatient)
                        + String.format("%-36s", idDoctor)
                        + String.format("%-10s", date.toString())
                        + String.format("%-8s", hourStr)
                        + String.format("%-150s", reason)
                        + String.format("%-15s", state)
                        + String.format("%-150s", obs)
                        + "\n";

                raf.write(line.getBytes(StandardCharsets.ISO_8859_1));
            }
            System.out.println("Generadas " + count + " citas en data/appointments.dat (Tamanio: " + raf.length() + " bytes)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateLogs(int count, List<String> doctorIds, List<String> patientIds) {
        Random random = new Random(888);
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String[] users = {
            "Ana Morales (249e0936-c830-431d-a446-f6dba664fa57)",
            "Laura Lopez (44b6603a-cbdb-4269-b307-f6cddda5a156)",
            "Sofia Perez (cf5c94f6-adf9-41af-8e5a-c78b4c14cd3a)",
            "Juan Perez (d3770d79-05a6-4331-9915-f1cf56ddd589)",
            "Carlos Garcia (cd0d9668-0f02-4b64-9dc2-e5f5cfcf8ffa)"
        };

        String[] modules = {"Pacientes", "Medicos", "Citas", "Autenticacion / Sistema"};
        String[] actions = {"Creacion", "Actualizacion", "Eliminacion", "Estado / Reprogramacion", "Login / Logout"};

        try (RandomAccessFile raf = new RandomAccessFile("data/logs.dat", "rw")) {
            raf.setLength(0);

            for (int i = 0; i < count; i++) {
                String id = UUID.randomUUID().toString();
                LocalDateTime ldt = LocalDateTime.now().minusHours(count - i).minusMinutes(random.nextInt(45));
                String dtStr = ldt.format(dtFormatter);

                String user = users[i % users.length];
                String module = modules[i % modules.length];
                String action;
                String details;

                switch (module) {
                    case "Pacientes":
                        action = (i % 3 == 0) ? "Creacion" : (i % 3 == 1 ? "Actualizacion" : "Eliminacion");
                        details = "Operacion de paciente " + patientIds.get(random.nextInt(patientIds.size())) + " realizada exitosamente";
                        break;
                    case "Medicos":
                        action = (i % 3 == 0) ? "Creacion" : (i % 3 == 1 ? "Actualizacion" : "Eliminacion");
                        details = "Operacion de medico " + doctorIds.get(random.nextInt(doctorIds.size())) + " registrada en sistema";
                        break;
                    case "Citas":
                        action = (i % 3 == 0) ? "Creacion" : (i % 3 == 1 ? "Estado / Reprogramacion" : "Eliminacion");
                        String snip = REASONS[i % REASONS.length];
                        if (snip.length() > 30) snip = snip.substring(0, 30) + "...";
                        details = "Gestion de cita medica con motivo: " + snip;
                        break;
                    default:
                        action = "Login / Logout";
                        details = (i % 2 == 0) ? "Inicio de sesion de recepcionista exitoso" : "Cierre de sesion en el sistema";
                        break;
                }

                String line = String.format("%-36s", id)
                        + String.format("%-19s", dtStr)
                        + String.format("%-60s", user)
                        + String.format("%-30s", module)
                        + String.format("%-35s", action)
                        + String.format("%-150s", details)
                        + "\n";

                raf.write(line.getBytes(StandardCharsets.ISO_8859_1));
            }
            System.out.println("Generados " + count + " logs en data/logs.dat (Tamanio: " + raf.length() + " bytes)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
