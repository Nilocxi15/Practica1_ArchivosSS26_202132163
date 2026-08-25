package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    private final String idAppointment;
    private final String idPatient;
    private final String idDoctor;
    private LocalDate date;
    private LocalTime hour;
    private String consultationReason;
    private String state;
    private String observations;

    public Appointment(String idAppointment, String idPatient, String idDoctor, LocalDate date, LocalTime hour, String consultationReason, String state, String observations) {
        this.idAppointment = idAppointment;
        this.idPatient = idPatient;
        this.idDoctor = idDoctor;
        this.date = date;
        this.hour = hour;
        this.consultationReason = consultationReason;
        this.state = state;
        this.observations = observations;
    }

    // Getters
    public String getIdAppointment() {
        return idAppointment;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public String getIdDoctor() {
        return idDoctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getHour() {
        return hour;
    }

    public String getConsultationReason() {
        return consultationReason;
    }

    public String getState() {
        return state;
    }

    public String getObservations() {
        return observations;
    }

    // Setters
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setHour(LocalTime hour) {
        this.hour = hour;
    }

    public void setConsultationReason(String consultationReason) {
        this.consultationReason = consultationReason;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

}
