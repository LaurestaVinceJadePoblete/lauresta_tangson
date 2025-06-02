package com.sk.system.ui;

import com.sk.system.model.Announcement;
import com.sk.system.service.AnnouncementService;

import javax.swing.*;
import java.awt.*;

public class AnnouncementForm extends JFrame {
    private final AnnouncementService service = new AnnouncementService();

    public AnnouncementForm() {
        setTitle("Create Announcement");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(250, 250, 250));

        // Title label
        JLabel header = new JLabel("New Announcement", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainPanel.add(header, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField();
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel titlePanel = new JPanel(new BorderLayout(5, 5));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(titleField, BorderLayout.CENTER);

        JLabel contentLabel = new JLabel("Content:");
        JTextArea contentArea = new JTextArea(8, 30);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(contentArea);

        JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
        contentPanel.setOpaque(false);
        contentPanel.add(contentLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        formPanel.add(titlePanel, BorderLayout.NORTH);
        formPanel.add(contentPanel, BorderLayout.CENTER);

        // Button
        JButton postButton = new JButton("Post Announcement");
        postButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(postButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        postButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            if (!title.isEmpty() && !content.isEmpty()) {
                service.createAnnouncement(new Announcement(title, content));
                new AnnouncementListForm().setVisible(true);
                titleField.setText("");
                contentArea.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Title and content must not be empty.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
