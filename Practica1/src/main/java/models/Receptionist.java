package models;

public class Receptionist {
    private final String id;
    private final String fullName;
    private final String password;

    public Receptionist(String id, String fullName, String password) {
        this.id = id;
        this.fullName = fullName;
        this.password = password;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }
    
}
