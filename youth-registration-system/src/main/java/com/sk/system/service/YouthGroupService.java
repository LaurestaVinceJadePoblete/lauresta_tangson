package com.sk.system.service;

import com.sk.system.database.DatabaseHelper;
import com.sk.system.model.YouthGroup;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class YouthGroupService {

    public void addGroup(YouthGroup group) {
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO youth_group (group_name) VALUES (?)")) {
            stmt.setString(1, group.getGroupName());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<YouthGroup> getAllGroups() {
        List<YouthGroup> groups = new ArrayList<>();
        try (var conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM youth_group")) {
            while (rs.next()) {
                groups.add(new YouthGroup(rs.getInt("id"), rs.getString("group_name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;
    }
}
