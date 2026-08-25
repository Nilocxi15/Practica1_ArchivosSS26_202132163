package GUI.reports;

import DTO.AppointmentsDto;
import DTO.DoctorsDto;
import DTO.PatientsDto;
import DTO.ReportsDto;
import GUI.Home;
import GUI.Login;
import GUI.appointments.HomeAppointments;
import GUI.doctors.HomeDoctors;
import GUI.patients.HomePatients;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import models.Appointment;
import models.Doctor;
import models.LogEntry;
import models.Patient;
import models.Receptionist;

public class HomeReports extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(HomeReports.class.getName());

    private Receptionist receptionist;

    /**
     * Creates new form HomeReports
     */
    public HomeReports() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Reportes");
        this.setupTableRenderers();
        this.updateFilterVisibilities();
        this.generatePatientReport();
        this.generateDoctorReport();
        this.generateAppointmentReport();
        this.generateLogReport();
    }

    // Constructor parametrizado para inyección de dependencias
    public HomeReports(String id, String fullname) {
        this.receptionist = new Receptionist(id, fullname, "");
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Módulo de Reportes - Hospital San Carlos");
        this.setupTableRenderers();
        this.updateFilterVisibilities();
        this.generatePatientReport();
        this.generateDoctorReport();
        this.generateAppointmentReport();
        this.generateLogReport();
    }

    private void setupTableRenderers() {
        JTable[] tables = {patientReportTable, doctorReportTable, appointmentReportTable, logReportTable};
        for (JTable table : tables) {
            if (table != null) {
                // Header style
                JTableHeader header = table.getTableHeader();
                if (header != null) {
                    header.setFont(new Font("Poppins", Font.BOLD, 12));
                    DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
                    if (headerRenderer != null) {
                        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                    }
                }
                // Cell renderer (center alignment)
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                table.setDefaultRenderer(Object.class, centerRenderer);
            }
        }
    }

    private void updateFilterVisibilities() {
        // Pacientes: Tipo de Sangre solo visible para reporte 1 (por tipo de sangre)
        int patientIndex = patientReportComboBox.getSelectedIndex();
        boolean showBlood = (patientIndex == 1);
        bloodTypeLabel.setVisible(showBlood);
        bloodTypeComboBox.setVisible(showBlood);

        // Médicos: Especialidad para reporte 1, Fecha para reporte 3
        int doctorIndex = doctorReportComboBox.getSelectedIndex();
        boolean showDoctorSpec = (doctorIndex == 1);
        boolean showDoctorDate = (doctorIndex == 3);
        doctorFilterLabel.setVisible(showDoctorSpec);
        doctorSpecialityTextField.setVisible(showDoctorSpec);
        doctorDateLabel.setVisible(showDoctorDate);
        doctorDateChooser.setVisible(showDoctorDate);

        // Citas: Rango de fechas para reporte 1, Filtro texto para 2 (médico) y 3 (paciente), Estado para 4
        int appointmentIndex = appointmentReportComboBox.getSelectedIndex();
        boolean showDateRange = (appointmentIndex == 1);
        boolean showFilterText = (appointmentIndex == 2 || appointmentIndex == 3);
        boolean showAppointmentState = (appointmentIndex == 4);

        startDateLabel.setVisible(showDateRange);
        startDateChooser.setVisible(showDateRange);
        endDateLabel.setVisible(showDateRange);
        endDateChooser.setVisible(showDateRange);

        appointmentFilterLabel.setVisible(showFilterText);
        appointmentFilterTextField.setVisible(showFilterText);
        if (showFilterText) {
            appointmentFilterLabel.setText(appointmentIndex == 2 ? "Médico:" : "Paciente:");
        }

        appointmentStateLabel.setVisible(showAppointmentState);
        appointmentStateComboBox.setVisible(showAppointmentState);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        reportsTabbedPane = new javax.swing.JTabbedPane();
        patientTabPanel = new javax.swing.JPanel();
        patientReportLabel = new javax.swing.JLabel();
        patientReportComboBox = new javax.swing.JComboBox<>();
        bloodTypeLabel = new javax.swing.JLabel();
        bloodTypeComboBox = new javax.swing.JComboBox<>();
        generatePatientReportBtn = new javax.swing.JButton();
        exportPatientReportBtn = new javax.swing.JButton();
        patientScrollPane = new javax.swing.JScrollPane();
        patientReportTable = new javax.swing.JTable();
        doctorTabPanel = new javax.swing.JPanel();
        doctorReportLabel = new javax.swing.JLabel();
        doctorReportComboBox = new javax.swing.JComboBox<>();
        doctorFilterLabel = new javax.swing.JLabel();
        doctorSpecialityTextField = new javax.swing.JTextField();
        doctorDateLabel = new javax.swing.JLabel();
        doctorDateChooser = new com.toedter.calendar.JDateChooser();
        generateDoctorReportBtn = new javax.swing.JButton();
        exportDoctorReportBtn = new javax.swing.JButton();
        doctorScrollPane = new javax.swing.JScrollPane();
        doctorReportTable = new javax.swing.JTable();
        appointmentTabPanel = new javax.swing.JPanel();
        appointmentReportLabel = new javax.swing.JLabel();
        appointmentReportComboBox = new javax.swing.JComboBox<>();
        startDateLabel = new javax.swing.JLabel();
        startDateChooser = new com.toedter.calendar.JDateChooser();
        endDateLabel = new javax.swing.JLabel();
        endDateChooser = new com.toedter.calendar.JDateChooser();
        appointmentFilterLabel = new javax.swing.JLabel();
        appointmentFilterTextField = new javax.swing.JTextField();
        appointmentStateLabel = new javax.swing.JLabel();
        appointmentStateComboBox = new javax.swing.JComboBox<>();
        generateAppointmentReportBtn = new javax.swing.JButton();
        exportAppointmentReportBtn = new javax.swing.JButton();
        appointmentScrollPane = new javax.swing.JScrollPane();
        appointmentReportTable = new javax.swing.JTable();
        logTabPanel = new javax.swing.JPanel();
        logModuleLabel = new javax.swing.JLabel();
        logModuleComboBox = new javax.swing.JComboBox<>();
        logActionLabel = new javax.swing.JLabel();
        logActionComboBox = new javax.swing.JComboBox<>();
        generateLogReportBtn = new javax.swing.JButton();
        exportLogReportBtn = new javax.swing.JButton();
        logScrollPane = new javax.swing.JScrollPane();
        logReportTable = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        filesMenu = new javax.swing.JMenu();
        homeMenu = new javax.swing.JMenu();
        patientsMenu = new javax.swing.JMenu();
        doctorsMenu = new javax.swing.JMenu();
        appointmentsMenu = new javax.swing.JMenu();
        reportsMenu = new javax.swing.JMenu();
        logoutMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1280, 720));
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setPreferredSize(new java.awt.Dimension(1280, 720));
        setResizable(false);

        titleLabel.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        titleLabel.setText("Módulo de Reportes");

        reportsTabbedPane.setFont(new java.awt.Font("Poppins", 1, 13)); // NOI18N

        patientReportLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        patientReportLabel.setText("Tipo de Reporte:");

        patientReportComboBox.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        patientReportComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Reporte completo de pacientes", "Reporte de pacientes por tipo de sangre", "Reporte de pacientes con mayor cantidad de citas", "Reporte de pacientes que nunca han tenido una cita" }));
        patientReportComboBox.addActionListener(this::patientReportComboBoxActionPerformed);

        bloodTypeLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        bloodTypeLabel.setText("Tipo de Sangre:");

        bloodTypeComboBox.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        bloodTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" }));
        bloodTypeComboBox.addActionListener(this::bloodTypeComboBoxActionPerformed);

        generatePatientReportBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        generatePatientReportBtn.setText("Generar Reporte");
        generatePatientReportBtn.addActionListener(this::generatePatientReportBtnActionPerformed);

        exportPatientReportBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        exportPatientReportBtn.setText("Exportar Datos");
        exportPatientReportBtn.addActionListener(this::exportPatientReportBtnActionPerformed);

        patientReportTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        patientReportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No. Identificación", "Nombres", "Apellidos", "Fecha Nacimiento", "Género", "Celular", "Tipo Sangre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        patientReportTable.setRowHeight(25);
        patientScrollPane.setViewportView(patientReportTable);

        javax.swing.GroupLayout patientTabPanelLayout = new javax.swing.GroupLayout(patientTabPanel);
        patientTabPanel.setLayout(patientTabPanelLayout);
        patientTabPanelLayout.setHorizontalGroup(
            patientTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(patientTabPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(patientTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(patientScrollPane)
                    .addGroup(patientTabPanelLayout.createSequentialGroup()
                        .addComponent(patientReportLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(patientReportComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bloodTypeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bloodTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 170, Short.MAX_VALUE)
                        .addComponent(generatePatientReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(exportPatientReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );
        patientTabPanelLayout.setVerticalGroup(
            patientTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(patientTabPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(patientTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(patientReportLabel)
                    .addComponent(patientReportComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bloodTypeLabel)
                    .addComponent(bloodTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generatePatientReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exportPatientReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(patientScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        reportsTabbedPane.addTab("Pacientes", patientTabPanel);

        doctorReportLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        doctorReportLabel.setText("Tipo de Reporte:");

        doctorReportComboBox.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        doctorReportComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Reporte completo de médicos", "Reporte de médicos por especialidad", "Reporte de médicos con mayor cantidad de citas", "Reporte de médicos con citas programadas para una fecha específica" }));
        doctorReportComboBox.addActionListener(this::doctorReportComboBoxActionPerformed);

        doctorFilterLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        doctorFilterLabel.setText("Especialidad:");

        doctorSpecialityTextField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        doctorDateLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        doctorDateLabel.setText("Fecha:");

        generateDoctorReportBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        generateDoctorReportBtn.setText("Generar Reporte");
        generateDoctorReportBtn.addActionListener(this::generateDoctorReportBtnActionPerformed);

        exportDoctorReportBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        exportDoctorReportBtn.setText("Exportar Datos");
        exportDoctorReportBtn.addActionListener(this::exportDoctorReportBtnActionPerformed);

        doctorReportTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        doctorReportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "UUID Médico", "Nombres", "Apellidos", "Especialidad", "Celular", "Horario Atención", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        doctorReportTable.setRowHeight(25);
        doctorScrollPane.setViewportView(doctorReportTable);

        javax.swing.GroupLayout doctorTabPanelLayout = new javax.swing.GroupLayout(doctorTabPanel);
        doctorTabPanel.setLayout(doctorTabPanelLayout);
        doctorTabPanelLayout.setHorizontalGroup(
            doctorTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(doctorTabPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(doctorTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(doctorScrollPane)
                    .addGroup(doctorTabPanelLayout.createSequentialGroup()
                        .addComponent(doctorReportLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(doctorReportComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(doctorFilterLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(doctorSpecialityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(doctorDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(doctorDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(generateDoctorReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(exportDoctorReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );
        doctorTabPanelLayout.setVerticalGroup(
            doctorTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(doctorTabPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(doctorTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(doctorTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(doctorReportLabel)
                        .addComponent(doctorReportComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(doctorFilterLabel)
                        .addComponent(doctorSpecialityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(doctorDateLabel)
                        .addComponent(generateDoctorReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(exportDoctorReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(doctorDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(doctorScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        reportsTabbedPane.addTab("Médicos", doctorTabPanel);

        appointmentReportLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        appointmentReportLabel.setText("Tipo de Reporte:");

        appointmentReportComboBox.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        appointmentReportComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Reporte completo de citas", "Reporte de citas por rango de fechas", "Reporte de citas por médico", "Reporte de citas por paciente", "Reporte de citas por estado", "Reporte de cantidad de citas por especialidad" }));
        appointmentReportComboBox.addActionListener(this::appointmentReportComboBoxActionPerformed);

        startDateLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        startDateLabel.setText("Desde:");

        endDateLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        endDateLabel.setText("Hasta:");

        appointmentFilterLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        appointmentFilterLabel.setText("Buscar:");

        appointmentFilterTextField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        appointmentStateLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        appointmentStateLabel.setText("Estado:");

        appointmentStateComboBox.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        appointmentStateComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Programada", "Atendida", "Cancelada" }));

        generateAppointmentReportBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        generateAppointmentReportBtn.setText("Generar Reporte");
        generateAppointmentReportBtn.addActionListener(this::generateAppointmentReportBtnActionPerformed);

        exportAppointmentReportBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        exportAppointmentReportBtn.setText("Exportar Datos");
        exportAppointmentReportBtn.addActionListener(this::exportAppointmentReportBtnActionPerformed);

        appointmentReportTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        appointmentReportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Cita", "Paciente", "Médico", "Fecha", "Hora Inicio", "Motivo", "Estado", "Observaciones"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        appointmentReportTable.setRowHeight(25);
        appointmentScrollPane.setViewportView(appointmentReportTable);

        javax.swing.GroupLayout appointmentTabPanelLayout = new javax.swing.GroupLayout(appointmentTabPanel);
        appointmentTabPanel.setLayout(appointmentTabPanelLayout);
        appointmentTabPanelLayout.setHorizontalGroup(
            appointmentTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(appointmentTabPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(appointmentTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(appointmentScrollPane)
                    .addGroup(appointmentTabPanelLayout.createSequentialGroup()
                        .addComponent(appointmentReportLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(appointmentReportComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(startDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(endDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(endDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(appointmentFilterLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(appointmentFilterTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(appointmentStateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(appointmentStateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(generateAppointmentReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportAppointmentReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );
        appointmentTabPanelLayout.setVerticalGroup(
            appointmentTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(appointmentTabPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(appointmentTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(appointmentTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(appointmentReportLabel)
                        .addComponent(appointmentReportComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(startDateLabel)
                        .addComponent(endDateLabel)
                        .addComponent(appointmentFilterLabel)
                        .addComponent(appointmentFilterTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(appointmentStateLabel)
                        .addComponent(appointmentStateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(generateAppointmentReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(exportAppointmentReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(startDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(endDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(appointmentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        reportsTabbedPane.addTab("Citas", appointmentTabPanel);

        logModuleLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        logModuleLabel.setText("Módulo:");

        logModuleComboBox.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        logModuleComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Pacientes", "Médicos", "Citas", "Autenticación / Sistema" }));

        logActionLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        logActionLabel.setText("Acción:");

        logActionComboBox.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        logActionComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Creación", "Actualización", "Eliminación", "Estado / Reprogramación", "Login / Logout" }));

        generateLogReportBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        generateLogReportBtn.setText("Generar Reporte");
        generateLogReportBtn.addActionListener(this::generateLogReportBtnActionPerformed);

        exportLogReportBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        exportLogReportBtn.setText("Exportar Datos");
        exportLogReportBtn.addActionListener(this::exportLogReportBtnActionPerformed);

        logReportTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        logReportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID / Timestamp", "Fecha y Hora", "Usuario / Rol", "Módulo", "Operación", "Descripción / Detalles"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        logReportTable.setRowHeight(25);
        logScrollPane.setViewportView(logReportTable);

        javax.swing.GroupLayout logTabPanelLayout = new javax.swing.GroupLayout(logTabPanel);
        logTabPanel.setLayout(logTabPanelLayout);
        logTabPanelLayout.setHorizontalGroup(
            logTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logTabPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(logTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logScrollPane)
                    .addGroup(logTabPanelLayout.createSequentialGroup()
                        .addComponent(logModuleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(logModuleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(logActionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(logActionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 280, Short.MAX_VALUE)
                        .addComponent(generateLogReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(exportLogReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );
        logTabPanelLayout.setVerticalGroup(
            logTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logTabPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(logTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logModuleLabel)
                    .addComponent(logModuleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logActionLabel)
                    .addComponent(logActionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generateLogReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exportLogReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(logScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        reportsTabbedPane.addTab("Bitácora de Logs", logTabPanel);

        filesMenu.setText("Archivos");
        filesMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filesMenuMouseClicked(evt);
            }
        });
        jMenuBar1.add(filesMenu);

        homeMenu.setText("Inicio");
        homeMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeMenuMouseClicked(evt);
            }
        });
        jMenuBar1.add(homeMenu);

        patientsMenu.setText("Pacientes");
        patientsMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                patientsMenuMouseClicked(evt);
            }
        });
        jMenuBar1.add(patientsMenu);

        doctorsMenu.setText("Médicos");
        doctorsMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                doctorsMenuMouseClicked(evt);
            }
        });
        jMenuBar1.add(doctorsMenu);

        appointmentsMenu.setText("Citas");
        appointmentsMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                appointmentsMenuMouseClicked(evt);
            }
        });
        jMenuBar1.add(appointmentsMenu);

        reportsMenu.setText("Reportes");
        jMenuBar1.add(reportsMenu);

        logoutMenu.setText("Cerrar Sesión");
        logoutMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutMenuMouseClicked(evt);
            }
        });
        jMenuBar1.add(logoutMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel)
                    .addComponent(reportsTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1220, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(reportsTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void filesMenuMouseClicked(java.awt.event.MouseEvent evt) {
        ReportsDto reports = new ReportsDto();
        try {
            reports.createFolder();
            reports.openFolder();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir la carpeta de reportes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void homeMenuMouseClicked(java.awt.event.MouseEvent evt) {
        this.dispose();
        if (receptionist != null) {
            Home home = new Home(receptionist.getId(), receptionist.getFullName());
            home.setVisible(true);
        } else {
            Home home = new Home();
            home.setVisible(true);
        }
    }

    private void patientsMenuMouseClicked(java.awt.event.MouseEvent evt) {
        this.dispose();
        if (receptionist != null) {
            HomePatients homePatients = new HomePatients(receptionist.getId(), receptionist.getFullName());
            homePatients.setVisible(true);
        } else {
            HomePatients homePatients = new HomePatients();
            homePatients.setVisible(true);
        }
    }

    private void doctorsMenuMouseClicked(java.awt.event.MouseEvent evt) {
        this.dispose();
        if (receptionist != null) {
            HomeDoctors homeDoctors = new HomeDoctors(receptionist.getId(), receptionist.getFullName());
            homeDoctors.setVisible(true);
        } else {
            HomeDoctors homeDoctors = new HomeDoctors();
            homeDoctors.setVisible(true);
        }
    }

    private void appointmentsMenuMouseClicked(java.awt.event.MouseEvent evt) {
        this.dispose();
        if (receptionist != null) {
            HomeAppointments homeAppointments = new HomeAppointments(receptionist.getId(), receptionist.getFullName());
            homeAppointments.setVisible(true);
        } else {
            HomeAppointments homeAppointments = new HomeAppointments();
            homeAppointments.setVisible(true);
        }
    }

    private void logoutMenuMouseClicked(java.awt.event.MouseEvent evt) {
        this.dispose();
        Login login = new Login();
        login.setVisible(true);
    }

    private void patientReportComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        updateFilterVisibilities();
        int index = patientReportComboBox.getSelectedIndex();
        DefaultTableModel model;

        switch (index) {
            case 0: // Completo
            case 1: // Por tipo de sangre
                model = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"No. Identificación", "Nombres", "Apellidos", "Fecha Nacimiento", "Género", "Celular", "Tipo Sangre"}
                ) {
                    public boolean isCellEditable(int r, int c) { return false; }
                };
                break;
            case 2: // Mayor cantidad de citas
                model = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"No. Identificación", "Nombre del Paciente", "Total Citas", "Citas Programadas", "Citas Atendidas", "Citas Canceladas"}
                ) {
                    public boolean isCellEditable(int r, int c) { return false; }
                };
                break;
            case 3: // Nunca han tenido cita
                model = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"No. Identificación", "Nombre Completo", "Celular", "Correo Electrónico", "Tipo Sangre", "Historial de Citas"}
                ) {
                    public boolean isCellEditable(int r, int c) { return false; }
                };
                break;
            default:
                return;
        }
        patientReportTable.setModel(model);
        setupTableRenderers();
        generatePatientReport();
    }

    private void bloodTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        generatePatientReport();
    }

    private void generatePatientReportBtnActionPerformed(java.awt.event.ActionEvent evt) {
        generatePatientReport();
        JOptionPane.showMessageDialog(this, "Reporte de pacientes generado correctamente.", "Reporte Generado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void generatePatientReport() {
        ReportsDto reportsDto = new ReportsDto();
        int index = patientReportComboBox.getSelectedIndex();
        DefaultTableModel model = (DefaultTableModel) patientReportTable.getModel();
        model.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        switch (index) {
            case 0: { // Completo
                ArrayList<Patient> list = reportsDto.reportPatientsComplete();
                for (Patient p : list) {
                    String date = (p.getBirthdate() != null) ? p.getBirthdate().format(dtf) : "";
                    model.addRow(new Object[]{p.getId(), p.getName(), p.getLastname(), date, p.getGender(), p.getCellphone(), p.getBloodType()});
                }
                break;
            }
            case 1: { // Por tipo de sangre
                String blood = (String) bloodTypeComboBox.getSelectedItem();
                ArrayList<Patient> list = reportsDto.reportPatientsByBloodType(blood);
                for (Patient p : list) {
                    String date = (p.getBirthdate() != null) ? p.getBirthdate().format(dtf) : "";
                    model.addRow(new Object[]{p.getId(), p.getName(), p.getLastname(), date, p.getGender(), p.getCellphone(), p.getBloodType()});
                }
                break;
            }
            case 2: { // Mayor cantidad de citas
                ArrayList<Object[]> list = reportsDto.reportPatientsMostAppointments();
                for (Object[] row : list) {
                    model.addRow(row);
                }
                break;
            }
            case 3: { // Nunca han tenido cita
                ArrayList<Patient> list = reportsDto.reportPatientsNoAppointments();
                for (Patient p : list) {
                    model.addRow(new Object[]{p.getId(), p.getName() + " " + p.getLastname(), p.getCellphone(), p.getEmail(), p.getBloodType(), "Sin citas registradas"});
                }
                break;
            }
        }
        setupTableRenderers();
    }

    private void exportPatientReportBtnActionPerformed(java.awt.event.ActionEvent evt) {
        exportTableData(patientReportTable, "Reporte_Pacientes_" + (String) patientReportComboBox.getSelectedItem());
    }

    private void doctorReportComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        updateFilterVisibilities();
        int index = doctorReportComboBox.getSelectedIndex();
        DefaultTableModel model;

        switch (index) {
            case 0: // Completo
            case 1: // Por especialidad
                model = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"UUID Médico", "Nombres", "Apellidos", "Especialidad", "Celular", "Horario Atención", "Estado"}
                ) {
                    public boolean isCellEditable(int r, int c) { return false; }
                };
                break;
            case 2: // Mayor cantidad de citas
                model = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"UUID Médico", "Nombre del Médico", "Especialidad", "Total Citas", "Citas Atendidas", "Citas Programadas"}
                ) {
                    public boolean isCellEditable(int r, int c) { return false; }
                };
                break;
            case 3: // Citas para fecha específica
                model = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"UUID Médico", "Nombre del Médico", "Especialidad", "Fecha Programada", "Hora Cita", "ID Paciente", "Motivo"}
                ) {
                    public boolean isCellEditable(int r, int c) { return false; }
                };
                break;
            default:
                return;
        }
        doctorReportTable.setModel(model);
        setupTableRenderers();
        generateDoctorReport();
    }

    private void generateDoctorReportBtnActionPerformed(java.awt.event.ActionEvent evt) {
        generateDoctorReport();
        JOptionPane.showMessageDialog(this, "Reporte de médicos generado correctamente.", "Reporte Generado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void generateDoctorReport() {
        ReportsDto reportsDto = new ReportsDto();
        int index = doctorReportComboBox.getSelectedIndex();
        DefaultTableModel model = (DefaultTableModel) doctorReportTable.getModel();
        model.setRowCount(0);
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

        switch (index) {
            case 0: { // Completo
                ArrayList<Doctor> list = reportsDto.reportDoctorsComplete();
                for (Doctor d : list) {
                    String schedule = (d.getStartShift() != null && d.getEndShift() != null)
                            ? d.getStartShift().format(tf) + " - " + d.getEndShift().format(tf) : "";
                    model.addRow(new Object[]{d.getId(), d.getName(), d.getLastname(), d.getSpeciality(), d.getCellphone(), schedule, d.isState() ? "Activo" : "Inactivo"});
                }
                break;
            }
            case 1: { // Por especialidad
                String spec = doctorSpecialityTextField.getText();
                ArrayList<Doctor> list = reportsDto.reportDoctorsBySpeciality(spec);
                for (Doctor d : list) {
                    String schedule = (d.getStartShift() != null && d.getEndShift() != null)
                            ? d.getStartShift().format(tf) + " - " + d.getEndShift().format(tf) : "";
                    model.addRow(new Object[]{d.getId(), d.getName(), d.getLastname(), d.getSpeciality(), d.getCellphone(), schedule, d.isState() ? "Activo" : "Inactivo"});
                }
                break;
            }
            case 2: { // Mayor cantidad de citas
                ArrayList<Object[]> list = reportsDto.reportDoctorsMostAppointments();
                for (Object[] row : list) {
                    model.addRow(row);
                }
                break;
            }
            case 3: { // Citas para fecha específica
                Date date = doctorDateChooser.getDate();
                if (date == null) {
                    return;
                }
                LocalDate targetDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                ArrayList<Object[]> list = reportsDto.reportDoctorsWithAppointmentsOnDate(targetDate);
                for (Object[] row : list) {
                    model.addRow(row);
                }
                break;
            }
        }
        setupTableRenderers();
    }

    private void exportDoctorReportBtnActionPerformed(java.awt.event.ActionEvent evt) {
        exportTableData(doctorReportTable, "Reporte_Medicos_" + (String) doctorReportComboBox.getSelectedItem());
    }

    private void appointmentReportComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        updateFilterVisibilities();
        int index = appointmentReportComboBox.getSelectedIndex();
        DefaultTableModel model;

        switch (index) {
            case 0: // Completo
            case 1: // Por rango de fechas
            case 2: // Por médico
            case 3: // Por paciente
            case 4: // Por estado
                model = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"ID Cita", "Paciente", "Médico", "Fecha", "Hora Inicio", "Motivo", "Estado", "Observaciones"}
                ) {
                    public boolean isCellEditable(int r, int c) { return false; }
                };
                break;
            case 5: // Cantidad de citas por especialidad
                model = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Especialidad", "Total Citas", "Citas Programadas", "Citas Atendidas", "Citas Canceladas"}
                ) {
                    public boolean isCellEditable(int r, int c) { return false; }
                };
                break;
            default:
                return;
        }
        appointmentReportTable.setModel(model);
        setupTableRenderers();
        generateAppointmentReport();
    }

    private void generateAppointmentReportBtnActionPerformed(java.awt.event.ActionEvent evt) {
        generateAppointmentReport();
        JOptionPane.showMessageDialog(this, "Reporte de citas generado correctamente.", "Reporte Generado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void generateAppointmentReport() {
        ReportsDto reportsDto = new ReportsDto();
        DoctorsDto doctorsDto = new DoctorsDto();
        PatientsDto patientsDto = new PatientsDto();
        int index = appointmentReportComboBox.getSelectedIndex();
        DefaultTableModel model = (DefaultTableModel) appointmentReportTable.getModel();
        model.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

        switch (index) {
            case 0: { // Completo
                populateAppointmentTable(reportsDto.reportAppointmentsComplete(), model, doctorsDto, patientsDto, dtf, tf);
                break;
            }
            case 1: { // Rango de fechas
                Date start = startDateChooser.getDate();
                Date end = endDateChooser.getDate();
                if (start == null || end == null) {
                    return;
                }
                LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (startDate.isAfter(endDate)) {
                    JOptionPane.showMessageDialog(this, "La fecha inicial no puede ser posterior a la fecha final.", "Fechas inválidas", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                populateAppointmentTable(reportsDto.reportAppointmentsByDateRange(startDate, endDate), model, doctorsDto, patientsDto, dtf, tf);
                break;
            }
            case 2: { // Por médico
                String query = appointmentFilterTextField.getText();
                populateAppointmentTable(reportsDto.reportAppointmentsByDoctor(query), model, doctorsDto, patientsDto, dtf, tf);
                break;
            }
            case 3: { // Por paciente
                String query = appointmentFilterTextField.getText();
                populateAppointmentTable(reportsDto.reportAppointmentsByPatient(query), model, doctorsDto, patientsDto, dtf, tf);
                break;
            }
            case 4: { // Por estado
                String state = (String) appointmentStateComboBox.getSelectedItem();
                populateAppointmentTable(reportsDto.reportAppointmentsByState(state), model, doctorsDto, patientsDto, dtf, tf);
                break;
            }
            case 5: { // Cantidad por especialidad
                ArrayList<Object[]> list = reportsDto.reportAppointmentsCountBySpeciality();
                for (Object[] row : list) {
                    model.addRow(row);
                }
                break;
            }
        }
        setupTableRenderers();
    }

    private void populateAppointmentTable(ArrayList<Appointment> appointments, DefaultTableModel model,
            DoctorsDto doctorsDto, PatientsDto patientsDto, DateTimeFormatter dtf, DateTimeFormatter tf) {
        if (appointments == null) return;
        for (Appointment a : appointments) {
            Patient p = patientsDto.searchRegister(a.getIdPatient());
            Doctor d = doctorsDto.searchRegister(a.getIdDoctor());
            String pName = (p != null) ? p.getName() + " " + p.getLastname() : a.getIdPatient();
            String dName = (d != null) ? d.getName() + " " + d.getLastname() : a.getIdDoctor();
            String fDate = (a.getDate() != null) ? a.getDate().format(dtf) : "";
            String fHour = (a.getHour() != null) ? a.getHour().format(tf) : "";
            model.addRow(new Object[]{
                a.getIdAppointment(),
                pName,
                dName,
                fDate,
                fHour,
                a.getConsultationReason(),
                a.getState(),
                a.getObservations()
            });
        }
    }

    private void exportAppointmentReportBtnActionPerformed(java.awt.event.ActionEvent evt) {
        exportTableData(appointmentReportTable, "Reporte_Citas_" + (String) appointmentReportComboBox.getSelectedItem());
    }

    private void generateLogReportBtnActionPerformed(java.awt.event.ActionEvent evt) {
        generateLogReport();
        JOptionPane.showMessageDialog(this, "Bitácora de logs actualizada correctamente.", "Logs Actualizados", JOptionPane.INFORMATION_MESSAGE);
    }

    private void generateLogReport() {
        ReportsDto reportsDto = new ReportsDto();
        String module = (String) logModuleComboBox.getSelectedItem();
        String action = (String) logActionComboBox.getSelectedItem();
        ArrayList<LogEntry> logs = reportsDto.readLogs(module, action);

        DefaultTableModel model = (DefaultTableModel) logReportTable.getModel();
        model.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        for (LogEntry l : logs) {
            String dt = (l.getDateTime() != null) ? l.getDateTime().format(dtf) : "";
            model.addRow(new Object[]{
                l.getId(),
                dt,
                l.getUser(),
                l.getModule(),
                l.getAction(),
                l.getDetails()
            });
        }
        setupTableRenderers();
    }

    private void exportLogReportBtnActionPerformed(java.awt.event.ActionEvent evt) {
        exportTableData(logReportTable, "Reporte_Bitacora_Logs");
    }

    private void exportTableData(JTable table, String reportTitle) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay datos en la tabla para exportar.\nPor favor, genere el reporte primero.", "Tabla vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cols = model.getColumnCount();
        String[] headers = new String[cols];
        for (int i = 0; i < cols; i++) {
            headers[i] = model.getColumnName(i);
        }

        ArrayList<Object[]> rows = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Object[] row = new Object[cols];
            for (int j = 0; j < cols; j++) {
                row[j] = model.getValueAt(i, j);
            }
            rows.add(row);
        }

        ReportsDto reportsDto = new ReportsDto();
        try {
            File exportedFile = reportsDto.exportToCSV(reportTitle, headers, rows);
            int choice = JOptionPane.showConfirmDialog(this,
                    "Reporte exportado exitosamente como archivo CSV:\n" + exportedFile.getAbsolutePath() + "\n\n¿Desea abrir la carpeta contenedora?",
                    "Exportación Exitosa", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                reportsDto.openFolder();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar los datos del reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new HomeReports().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel appointmentFilterLabel;
    private javax.swing.JTextField appointmentFilterTextField;
    private javax.swing.JComboBox<String> appointmentReportComboBox;
    private javax.swing.JLabel appointmentReportLabel;
    private javax.swing.JTable appointmentReportTable;
    private javax.swing.JScrollPane appointmentScrollPane;
    private javax.swing.JComboBox<String> appointmentStateComboBox;
    private javax.swing.JLabel appointmentStateLabel;
    private javax.swing.JPanel appointmentTabPanel;
    private javax.swing.JMenu appointmentsMenu;
    private javax.swing.JComboBox<String> bloodTypeComboBox;
    private javax.swing.JLabel bloodTypeLabel;
    private com.toedter.calendar.JDateChooser doctorDateChooser;
    private javax.swing.JLabel doctorDateLabel;
    private javax.swing.JLabel doctorFilterLabel;
    private javax.swing.JComboBox<String> doctorReportComboBox;
    private javax.swing.JLabel doctorReportLabel;
    private javax.swing.JTable doctorReportTable;
    private javax.swing.JScrollPane doctorScrollPane;
    private javax.swing.JTextField doctorSpecialityTextField;
    private javax.swing.JPanel doctorTabPanel;
    private javax.swing.JMenu doctorsMenu;
    private com.toedter.calendar.JDateChooser endDateChooser;
    private javax.swing.JLabel endDateLabel;
    private javax.swing.JButton exportAppointmentReportBtn;
    private javax.swing.JButton exportDoctorReportBtn;
    private javax.swing.JButton exportLogReportBtn;
    private javax.swing.JButton exportPatientReportBtn;
    private javax.swing.JMenu filesMenu;
    private javax.swing.JButton generateAppointmentReportBtn;
    private javax.swing.JButton generateDoctorReportBtn;
    private javax.swing.JButton generateLogReportBtn;
    private javax.swing.JButton generatePatientReportBtn;
    private javax.swing.JMenu homeMenu;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JComboBox<String> logActionComboBox;
    private javax.swing.JLabel logActionLabel;
    private javax.swing.JComboBox<String> logModuleComboBox;
    private javax.swing.JLabel logModuleLabel;
    private javax.swing.JTable logReportTable;
    private javax.swing.JScrollPane logScrollPane;
    private javax.swing.JPanel logTabPanel;
    private javax.swing.JMenu logoutMenu;
    private javax.swing.JComboBox<String> patientReportComboBox;
    private javax.swing.JLabel patientReportLabel;
    private javax.swing.JTable patientReportTable;
    private javax.swing.JScrollPane patientScrollPane;
    private javax.swing.JPanel patientTabPanel;
    private javax.swing.JMenu patientsMenu;
    private javax.swing.JMenu reportsMenu;
    private javax.swing.JTabbedPane reportsTabbedPane;
    private com.toedter.calendar.JDateChooser startDateChooser;
    private javax.swing.JLabel startDateLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
