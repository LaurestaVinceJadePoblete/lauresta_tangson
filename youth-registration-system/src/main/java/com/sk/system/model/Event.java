package com.sk.system.model;

import java.time.LocalDateTime;

public class Event {
    private int id;
    private String title;
    private LocalDateTime dateTime;
    private String location;
    private String description;

    public Event(int eventId, String name, String date, String location1, String description1) {}

    public Event(int id, String title, LocalDateTime dateTime, String location, String description) {
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.description = description;
    }

    public Event(String title, LocalDateTime dateTime, String location, String description) {
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.description = description;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
    return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public LocalDateTime getDate() {
        return dateTime;
    }

    public void setDate(LocalDateTime date) {
        this.dateTime = date;
    }

}
