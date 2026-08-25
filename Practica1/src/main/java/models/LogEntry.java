package models;

import java.time.LocalDateTime;

public class LogEntry {

    private String id;
    private LocalDateTime dateTime;
    private String user;
    private String module;
    private String action;
    private String details;

    public LogEntry(String id, LocalDateTime dateTime, String user, String module, String action, String details) {
        this.id = id;
        this.dateTime = dateTime;
        this.user = user;
        this.module = module;
        this.action = action;
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
