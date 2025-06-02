package com.sk.system.service;

import com.sk.system.database.DatabaseHelper;
import com.sk.system.model.EducationalAssistance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EducationalAssistanceService {
    private Connection conn;

    public EducationalAssistanceService(Connection conn) {
        this.conn = conn;
    }

    public EducationalAssistanceService() throws SQLException {
        // Replace with your DB credentials and URL
        String url = "jdbc:mysql://localhost:3306/youth_db";
String user = "root";      // empty username
String password = "";  // empty password

this.conn = DriverManager.getConnection(url, user, password);
    }

    public void addAssistance(EducationalAssistance ea) throws SQLException {
        String sql = "INSERT INTO educational_assistance " +
                "(member_id, assistance_type, amount, date_given, remarks, school_name, education_level) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ea.getMemberId());
            stmt.setString(2, ea.getAssistanceType());
            stmt.setDouble(3, ea.getAmount());
            stmt.setDate(4, java.sql.Date.valueOf(ea.getDateGiven()));
            stmt.setString(5, ea.getRemarks());
            stmt.setString(6, ea.getSchoolName());
            stmt.setString(7, ea.getEducationLevel());
            stmt.executeUpdate();
        }
    }

    public List<EducationalAssistance> getAllAssistance() throws SQLException {
        List<EducationalAssistance> list = new ArrayList<>();
        String sql = "SELECT * FROM educational_assistance";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EducationalAssistance ea = new EducationalAssistance();
                ea.setId(rs.getInt("id"));
                ea.setMemberId(rs.getInt("member_id"));
                ea.setAssistanceType(rs.getString("assistance_type"));
                ea.setAmount(rs.getDouble("amount"));
                ea.setDateGiven(rs.getDate("date_given").toLocalDate());
                ea.setRemarks(rs.getString("remarks"));
                ea.setSchoolName(rs.getString("school_name"));
                ea.setEducationLevel(rs.getString("education_level"));
                list.add(ea);
            }
        }
        return list;
    }

public void deleteAssistance(int id) throws SQLException {
    String sql = "DELETE FROM educational_assistance WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}

}
