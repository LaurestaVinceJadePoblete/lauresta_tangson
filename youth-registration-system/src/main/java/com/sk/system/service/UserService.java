package com.sk.system.service;

import com.sk.system.database.DatabaseHelper;
import com.sk.system.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserService {

    public static boolean validateLogin(String user, String pass) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean registerUser(User user) throws Exception {
        if (userExists(user.getUsername())) return false;

        String sql = "INSERT INTO users (id, username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashPassword(user.getPasswordHash()));
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticateUser(String username, String password) throws Exception {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                return storedHash.equals(hashPassword(password));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean userExists(String username) throws Exception {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
