package com.sk.system.ui;

import com.sk.system.model.Event;
import com.sk.system.service.EventService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventListForm extends JFrame {
    private final EventService service = new EventService();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Date", "Location", "Description"}, 0);
    private JTable table;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public EventListForm() throws Exception {
        setTitle("Event Management");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        loadEvents();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addBtn = new JButton("Add Event");
        JButton updateBtn = new JButton("Update Event");
        JButton deleteBtn = new JButton("Delete Event");

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(panel);

        addBtn.addActionListener(e -> {
            EventForm eventForm = new EventForm(this, "Add Event", null);
            eventForm.setVisible(true);
            if (eventForm.isSaved()) {
                try {
                    service.addEvent(eventForm.getEvent());
                    loadEvents();
                } catch (Exception ex) {
                    Logger.getLogger(EventListForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        updateBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int eventId = (int) tableModel.getValueAt(selectedRow, 0);
                String name = (String) tableModel.getValueAt(selectedRow, 1);
                String dateStr = (String) tableModel.getValueAt(selectedRow, 2);
                String location = (String) tableModel.getValueAt(selectedRow, 3);
                String description = (String) tableModel.getValueAt(selectedRow, 4);

                try {
                    LocalDateTime date = LocalDateTime.parse(dateStr, formatter);
                    Event event = new Event(eventId, name, date, location, description);
                    EventForm eventForm = new EventForm(this, "Update Event", event);
                    eventForm.setVisible(true);

                    if (eventForm.isSaved()) {
                        service.updateEvent(eventForm.getEvent());
                        loadEvents();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(EventListForm.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Error parsing date for update.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an event to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int eventId = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this event?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        service.deleteEvent(eventId);
                        loadEvents();
                    } catch (Exception ex) {
                        Logger.getLogger(EventListForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an event to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void loadEvents() throws Exception {
        tableModel.setRowCount(0);
        List<Event> events = service.getAllEvents();
        for (Event event : events) {
            tableModel.addRow(new Object[]{
                    event.getId(),
                    event.getName(),
                    event.getDate().format(formatter),
                    event.getLocation(),
                    event.getDescription()
            });
        }
    }
}
