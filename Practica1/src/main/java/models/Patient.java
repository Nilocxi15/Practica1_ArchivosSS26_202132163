package models;

import java.time.LocalDate;

public class Patient {

    private final String ID;
    private String name;
    private String lastname;
    private LocalDate birthdate;
    private String gender;
    private String cellphone;
    private String email;
    private String bloodType;

    public Patient(String ID, String name, String lastname, LocalDate birthdate, String gender, String cellphone, String email, String bloodType) {
        this.ID = ID;
        this.name = name;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.gender = gender;
        this.cellphone = cellphone;
        this.email = email;
        this.bloodType = bloodType;
    }

    // Getters
    public String getID() {
        return ID;
    }

    public String getId() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getGender() {
        return gender;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getEmail() {
        return email;
    }

    public String getBloodType() {
        return bloodType;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

}