package models;

import java.time.LocalTime;

public class Doctor {
    private final String id;
    private String name;
    private String lastname;
    private String speciality;
    private String cellphone;
    private String email;
    private LocalTime startShift;
    private LocalTime endShift;
    private boolean state;

    public Doctor(String id, String name, String lastname, String speciality, String cellphone, String email, LocalTime startShift, LocalTime endShift, boolean state) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.speciality = speciality;
        this.cellphone = cellphone;
        this.email = email;
        this.startShift = startShift;
        this.endShift = endShift;
        this.state = state;
    }
    
    // Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getEmail() {
        return email;
    }

    public LocalTime getStartShift() {
        return startShift;
    }

    public LocalTime getEndShift() {
        return endShift;
    }

    public boolean isState() {
        return state;
    }
    
    // Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStartShift(LocalTime startShift) {
        this.startShift = startShift;
    }

    public void setEndShift(LocalTime endShift) {
        this.endShift = endShift;
    }

    public void setState(boolean state) {
        this.state = state;
    }
    
    
}
