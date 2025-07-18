package com.sk.system.model;

public class Project {
    private int id;
    private String title;
    private String description;
    private String date;

    public Project() {}

    public Project(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public Project(int id, String title, String description, String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
