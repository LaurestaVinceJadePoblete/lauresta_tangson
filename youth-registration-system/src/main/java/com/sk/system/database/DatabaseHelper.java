package com.sk.system.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {

    private static final String URL = "jdbc:mysql://localhost:3306/youth_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
    try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (username VARCHAR(100), password VARCHAR(100))");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS members (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), age INT, address VARCHAR(255), contact VARCHAR(100))");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS announcements (id INT PRIMARY KEY AUTO_INCREMENT, title VARCHAR(255), content TEXT)");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS events ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "title VARCHAR(255), "
                + "date DATETIME, "
                + "location VARCHAR(255), "
                + "description TEXT)");

        // Add this block for Educational Assistance
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS educational_assistance ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "member_id INT, "
                + "assistance_type VARCHAR(100), "
                + "amount DOUBLE, "
                + "date_given DATE, "
                + "remarks TEXT, "
                + "school_name VARCHAR(255), "
                + "education_level VARCHAR(100), "
                + "FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE)"
        );

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    public static void init() {
        initializeDatabase();
    }

    public static Connection connect() {
    try {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}

public static List<Map<String, Object>> getEducationalAssistanceWithNames() {
    List<Map<String, Object>> list = new ArrayList<>();
    String sql = "SELECT ea.*, m.name FROM educational_assistance ea " +
                 "JOIN members m ON ea.member_id = m.id";

    try (Connection conn = connect();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", rs.getInt("id"));
            row.put("member_id", rs.getInt("member_id"));
            row.put("name", rs.getString("name")); // 👈 member name
            row.put("assistance_type", rs.getString("assistance_type"));
            row.put("amount", rs.getDouble("amount"));
            row.put("date_given", rs.getString("date_given"));
            row.put("remarks", rs.getString("remarks"));
            row.put("school_name", rs.getString("school_name"));
            row.put("education_level", rs.getString("education_level"));
            list.add(row);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}


}
