package com.sk.system.model;

import com.sk.system.service.GroupService;
import java.util.List;

public class Group {
    private int id;
    private String name;
    private String description;
    private List<Member> members;  // cache members list

    public Group(String name1, String desc) {}

    public Group(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }

    // Lazy-load members list for this group
    public List<Member> getMembers() throws Exception {
        if (members == null) {
            GroupService gs = new GroupService();
            members = gs.getGroupMembers(this.id);
        }
        return members;
    }
}
