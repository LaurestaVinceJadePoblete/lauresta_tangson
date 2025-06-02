package com.sk.system.ui;

import com.sk.system.model.Event;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EventForm extends JDialog {
    private JTextField nameField = new JTextField(20);
    private JTextField dateField = new JTextField(20);
    private JTextField locationField = new JTextField(20);
    private JTextArea descriptionArea = new JTextArea(5, 20);

    private boolean saved = false;
    private Event event;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public EventForm(Frame owner, String title, Event event) {
        super(owner, title, true);
        this.event = event;

        initUI();

        if (event != null) {
            loadEvent(event);
        }

        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Date (YYYY-MM-DD HH:MM):"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        panel.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        panel.add(scrollPane, gbc);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        saveBtn.addActionListener(e -> {
            if (validateInputs()) {
                try {
                    LocalDateTime dateTime = LocalDateTime.parse(dateField.getText(), formatter);
                    saved = true;

                    if (this.event == null) {
                        this.event = new Event(
                                nameField.getText(),
                                dateTime,
                                locationField.getText(),
                                descriptionArea.getText()
                        );
                    } else {
                        this.event.setName(nameField.getText());
                        this.event.setDate(dateTime);
                        this.event.setLocation(locationField.getText());
                        this.event.setDescription(descriptionArea.getText());
                    }

                    dispose();
                } catch (DateTimeParseException ex) {
                    showError("Invalid date format. Please use YYYY-MM-DD HH:MM.");
                }
            }
        });

        cancelBtn.addActionListener(e -> dispose());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadEvent(Event event) {
        nameField.setText(event.getName());
        dateField.setText(event.getDate().format(formatter));
        locationField.setText(event.getLocation());
        descriptionArea.setText(event.getDescription());
    }

    private boolean validateInputs() {
        if (nameField.getText().trim().isEmpty()) {
            showError("Name cannot be empty");
            return false;
        }
        if (dateField.getText().trim().isEmpty()) {
            showError("Date cannot be empty");
            return false;
        }
        if (locationField.getText().trim().isEmpty()) {
            showError("Location cannot be empty");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isSaved() {
        return saved;
    }

    public Event getEvent() {
        return event;
    }
}
