package com.sk.system.model;

public class YouthGroup {
    private int id;
    private String groupName;

    public YouthGroup() {}

    public YouthGroup(String groupName) {
        this.groupName = groupName;
    }

    public YouthGroup(int id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    // Getters and setters...

    public String getGroupName() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
