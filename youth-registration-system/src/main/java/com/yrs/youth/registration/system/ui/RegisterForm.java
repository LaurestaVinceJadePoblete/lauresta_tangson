package com.yrs.youth.registration.system.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterForm extends JFrame {
    public RegisterForm() {
        setTitle("Youth Registration System - Register");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Create an Account", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        formPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JLabel confirmLabel = new JLabel("Confirm Password:");
        JPasswordField confirmField = new JPasswordField();
        JButton registerButton = new JButton("Register");

        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        userLabel.setFont(font);
        passLabel.setFont(font);
        confirmLabel.setFont(font);
        userField.setFont(font);
        passField.setFont(font);
        confirmField.setFont(font);
        registerButton.setFont(font);

        formPanel.add(userLabel);     formPanel.add(userField);
        formPanel.add(passLabel);     formPanel.add(passField);
        formPanel.add(confirmLabel);  formPanel.add(confirmField);
        formPanel.add(new JLabel());  formPanel.add(registerButton);

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);

        registerButton.addActionListener((ActionEvent e) -> {
            String pass = new String(passField.getPassword());
            String confirm = new String(confirmField.getPassword());

            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                dispose();
                new LoginForm().setVisible(true);
            }
        });
    }
}
