package com.sk.system.model;

public class Announcement {
    private int id;
    private String title;
    private String content;

    public Announcement() {}

    public Announcement(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Announcement(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
