package com.sk.system.service;

import com.sk.system.database.DatabaseHelper;
import com.sk.system.model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectService {

    public void addProject(Project project) throws Exception {
        String sql = "INSERT INTO projects (title, description, date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, project.getTitle());
            stmt.setString(2, project.getDescription());
            stmt.setString(3, project.getDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Project> getAllProjects() throws Exception {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT * FROM projects ORDER BY date DESC";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Project(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Project getProjectByTitleAndDate(String title, String date) throws Exception {
        String sql = "SELECT * FROM projects WHERE title = ? AND date = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Project(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("date")
                    );
                }
            }
        }
        return null;
    }

    // ✅ Implement deleteProject by ID
    public void deleteProject(int id) throws Exception {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Implement updateProject
    public void updateProject(Project updated) throws Exception {
        String sql = "UPDATE projects SET title = ?, description = ?, date = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, updated.getTitle());
            stmt.setString(2, updated.getDescription());
            stmt.setString(3, updated.getDate());
            stmt.setInt(4, updated.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
