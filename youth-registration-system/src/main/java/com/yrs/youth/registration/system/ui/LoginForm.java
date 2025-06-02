package com.yrs.youth.registration.system.ui;

import com.sk.system.ui.MainApp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginForm extends JFrame {
    public LoginForm() {
        setTitle("Youth Registration System - Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        userLabel.setFont(font);
        passLabel.setFont(font);
        userField.setFont(font);
        passField.setFont(font);
        loginButton.setFont(font);
//        registerButton.setFont(font);

        formPanel.add(userLabel);     formPanel.add(userField);
        formPanel.add(passLabel);     formPanel.add(passField);
        formPanel.add(loginButton);   //formPanel.add(registerButton);

        JLabel title = new JLabel("Login to your account", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);

        loginButton.addActionListener((ActionEvent e) -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword());

            if ("admin".equals(user) && "admin".equals(pass)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose();
                new MainApp().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

//        registerButton.addActionListener(e -> {
//            dispose();
//            new RegisterForm().setVisible(true);
//        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
