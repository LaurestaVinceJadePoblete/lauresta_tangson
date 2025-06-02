package com.sk.system.service;

import com.sk.system.database.DatabaseHelper;
import com.sk.system.model.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService {

    public void addEvent(Event event) throws Exception {
    String sql = "INSERT INTO events (name, date_time, location, description) VALUES (?, ?, ?, ?)";
    try (Connection conn = DatabaseHelper.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, event.getName());  // use getName() for consistency
        ps.setTimestamp(2, Timestamp.valueOf(event.getDate()));
        ps.setString(3, event.getLocation());
        ps.setString(4, event.getDescription());
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public void updateEvent(Event event) throws Exception {
    String sql = "UPDATE events SET name=?, date_time=?, location=?, description=? WHERE id=?";
    try (Connection conn = DatabaseHelper.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, event.getName());
        ps.setTimestamp(2, Timestamp.valueOf(event.getDate()));
        ps.setString(3, event.getLocation());
        ps.setString(4, event.getDescription());
        ps.setInt(5, event.getId());

        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public List<Event> getAllEvents() throws Exception {
    List<Event> events = new ArrayList<>();
    String sql = "SELECT * FROM events";
    try (Connection conn = DatabaseHelper.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            events.add(new Event(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getTimestamp("date_time").toLocalDateTime(),
                rs.getString("location"),
                rs.getString("description")
            ));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return events;
}

    public void deleteEvent(int id) throws Exception {
    String sql = "DELETE FROM events WHERE id=?";
    try (Connection conn = DatabaseHelper.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, id);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
