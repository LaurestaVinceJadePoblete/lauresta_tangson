package com.sk.system.ui;

import com.sk.system.model.EducationalAssistance;
import com.sk.system.service.EducationalAssistanceService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.sql.Connection;

public class EducationalAssistanceForm extends JFrame {
    private JTextField memberIdField, typeField, amountField, remarksField, schoolNameField, educationLevelField;
    private JButton saveButton;

    private EducationalAssistanceService assistanceService;

    public EducationalAssistanceForm(Connection conn, int memberId) {
        this.assistanceService = new EducationalAssistanceService(conn);

        setTitle("Educational Assistance Form");
        setSize(400, 350);
        setLayout(new GridLayout(8, 2, 5, 5));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new JLabel("Member ID:"));
        memberIdField = new JTextField(String.valueOf(memberId));
        memberIdField.setEditable(false);
        add(memberIdField);

        add(new JLabel("Assistance Type:"));
        typeField = new JTextField();
        add(typeField);

        add(new JLabel("Amount:"));
        amountField = new JTextField();
        add(amountField);

        add(new JLabel("School Name:"));
        schoolNameField = new JTextField();
        add(schoolNameField);

        add(new JLabel("Education Level:"));
        educationLevelField = new JTextField();
        add(educationLevelField);

        add(new JLabel("Remarks:"));
        remarksField = new JTextField();
        add(remarksField);

        add(new JLabel()); // Empty label for spacing
        saveButton = new JButton("Save");
        add(saveButton);

        saveButton.addActionListener(this::handleSave);

        setVisible(true);
    }

    private void handleSave(ActionEvent e) {
        try {
            EducationalAssistance ea = new EducationalAssistance();
            ea.setMemberId(Integer.parseInt(memberIdField.getText()));
            ea.setAssistanceType(typeField.getText());
            ea.setAmount(Double.parseDouble(amountField.getText()));
            ea.setDateGiven(LocalDate.now());
            ea.setRemarks(remarksField.getText());
            ea.setSchoolName(schoolNameField.getText());
            ea.setEducationLevel(educationLevelField.getText());

            assistanceService.addAssistance(ea);
            JOptionPane.showMessageDialog(this, "Educational assistance added successfully.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
