package com.sk.system.service;

import com.sk.system.database.DatabaseHelper;
import com.sk.system.model.Announcement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementService {

    public void createAnnouncement(Announcement a) {
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO announcements (title, content) VALUES (?, ?)")) {
            stmt.setString(1, a.getTitle());
            stmt.setString(2, a.getContent());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Announcement> getAllAnnouncements() {
        List<Announcement> list = new ArrayList<>();
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM announcements")) {

            while (rs.next()) {
                list.add(new Announcement(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

public void deleteAnnouncement(int id) {
    try (Connection conn = DatabaseHelper.getConnection();
         PreparedStatement stmt = conn.prepareStatement("DELETE FROM announcements WHERE id = ?")) {
        stmt.setInt(1, id);
        stmt.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
}
