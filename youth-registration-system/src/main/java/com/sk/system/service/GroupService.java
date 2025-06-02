package com.sk.system.service;

import com.sk.system.database.DatabaseHelper;
import com.sk.system.model.Group;
import com.sk.system.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupService {

    public void addGroup(Group group) throws Exception {
        String sql = "INSERT INTO groups (name, description) VALUES (?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, group.getName());
            stmt.setString(2, group.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Group> getAllGroups() throws Exception {
        List<Group> list = new ArrayList<>();
        String sql = "SELECT * FROM groups";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Group(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean assignMemberToGroup(int groupId, int memberId) throws Exception {
        String checkSql = "SELECT COUNT(*) FROM group_members WHERE group_id = ? AND member_id = ?";
        String insertSql = "INSERT INTO group_members (group_id, member_id) VALUES (?, ?)";

        try (Connection conn = DatabaseHelper.getConnection()) {
            // Check if member is already in the group
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, groupId);
                checkStmt.setInt(2, memberId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // already assigned
                }
            }

            // Assign member to group
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, groupId);
                insertStmt.setInt(2, memberId);
                insertStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // in case of SQL exception
    }

    public List<Member> getGroupMembers(int groupId) throws Exception {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT m.* FROM members m " +
                     "JOIN group_members gm ON m.id = gm.member_id " +
                     "WHERE gm.group_id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Member m = new Member(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("address"),
                    rs.getString("contact"),
                    rs.getString("gender"),
                    rs.getString("birthdate"),
                    rs.getString("school")
                );
                // Set group for the member
                m.setGroup(new Group(groupId, null, null)); // You can fetch full group if needed
                members.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    // Get the group of a member
    public Group getMemberGroup(int memberId) throws Exception {
        String sql = "SELECT g.id, g.name, g.description FROM groups g " +
                     "JOIN group_members gm ON g.id = gm.group_id " +
                     "WHERE gm.member_id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Group(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description")
                );
            }
        }
        return null;  // no group assigned
    }

    // Remove member from group
    public boolean removeMemberFromGroup(int groupId, int memberId) throws Exception {
        String sql = "DELETE FROM group_members WHERE group_id = ? AND member_id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            stmt.setInt(2, memberId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // ✅ Edit group (NEW)
    public void updateGroup(Group group) throws Exception {
        String sql = "UPDATE groups SET name = ?, description = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, group.getName());
            stmt.setString(2, group.getDescription());
            stmt.setInt(3, group.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Delete group (NEW)
    public void deleteGroup(int groupId) throws Exception {
        // First remove all members assigned to this group (optional but clean)
        String deleteMembersSql = "DELETE FROM group_members WHERE group_id = ?";
        String deleteGroupSql = "DELETE FROM groups WHERE id = ?";

        try (Connection conn = DatabaseHelper.getConnection()) {
            try (PreparedStatement stmt1 = conn.prepareStatement(deleteMembersSql)) {
                stmt1.setInt(1, groupId);
                stmt1.executeUpdate();
            }

            try (PreparedStatement stmt2 = conn.prepareStatement(deleteGroupSql)) {
                stmt2.setInt(1, groupId);
                stmt2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
