package com.sk.system.service;

import com.sk.system.database.DatabaseHelper;
import com.sk.system.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberService {

    public void addMember(Member member) {
        String sql = "INSERT INTO members(name, age, address, contact, gender, birthdate, school) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getName());
            stmt.setInt(2, member.getAge());
            stmt.setString(3, member.getAddress());
            stmt.setString(4, member.getContact());
            stmt.setString(5, member.getGender());
            stmt.setString(6, member.getBirthdate());
            stmt.setString(7, member.getSchool());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding member: " + e.getMessage());
        }
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = DatabaseHelper.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("address"),
                        rs.getString("contact"),
                        rs.getString("gender"),
                        rs.getString("birthdate"),
                        rs.getString("school")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving members: " + e.getMessage());
        }
        return members;
    }

    public void updateMember(Member member) {
        String sql = "UPDATE members SET name=?, age=?, address=?, contact=?, gender=?, birthdate=? WHERE id=?";
        try (Connection conn = DatabaseHelper.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getName());
            stmt.setInt(2, member.getAge());
            stmt.setString(3, member.getAddress());
            stmt.setString(4, member.getContact());
            stmt.setString(5, member.getGender());
            stmt.setString(6, member.getBirthdate());
            stmt.setString(7, member.getSchool());
            stmt.setInt(8, member.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
        }
    }

    public void deleteMember(int id) {
        String sql = "DELETE FROM members WHERE id=?";
        try (Connection conn = DatabaseHelper.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting member: " + e.getMessage());
        }
    }

    public Member getMemberById(int memberId) {
    Member member = null;
    try (Connection conn = DatabaseHelper.connect()) {
        String sql = "SELECT * FROM members WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, memberId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            member = new Member();
            member.setId(rs.getInt("id"));
            member.setName(rs.getString("name")); // 👈 CORRECTED
            member.setAge(rs.getInt("age"));
            member.setAddress(rs.getString("address"));
            member.setContact(rs.getString("contact"));
            member.setGender(rs.getString("gender"));
            member.setBirthdate(rs.getString("birthdate"));
            member.setSchool(rs.getString("school"));
        }
    } catch (SQLException e) {
        System.err.println("Error fetching member by ID: " + e.getMessage());
    }
    return member;
}

}