package com.sk.system.ui;

import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sk.system.model.Project;
import com.sk.system.service.ProjectService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.List;

public class ProjectForm extends JFrame {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final ProjectService projectService = new ProjectService();

    public ProjectForm() throws Exception {
        setTitle("Projects List");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Description", "Date"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton printProposalButton = new JButton("Print as Proposal");

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(printProposalButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data from DB
        loadProjectData();

        // Edit button action
        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) table.getValueAt(row, 0);
                String title = (String) table.getValueAt(row, 1);
                String desc = (String) table.getValueAt(row, 2);
                String date = (String) table.getValueAt(row, 3);

                JTextField titleField = new JTextField(title);
                JTextArea descArea = new JTextArea(desc, 5, 20);
                JTextField dateField = new JTextField(date);

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Title:"));
                panel.add(titleField);
                panel.add(new JLabel("Description:"));
                panel.add(new JScrollPane(descArea));
                panel.add(new JLabel("Date:"));
                panel.add(dateField);

                int result = JOptionPane.showConfirmDialog(this, panel, "Edit Project",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        Project updated = new Project(id, titleField.getText(), descArea.getText(), dateField.getText());
                        projectService.updateProject(updated);
                        loadProjectData();
                        JOptionPane.showMessageDialog(this, "Project updated.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a project to edit.");
            }
        });

        // Delete button action
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this project?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (int) table.getValueAt(row, 0);
                    try {
                        projectService.deleteProject(id);
                        loadProjectData();
                        JOptionPane.showMessageDialog(this, "Project deleted.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Deletion failed: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a project to delete.");
            }
        });

        // Print as Proposal button action
        printProposalButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) table.getValueAt(row, 0);
                String title = (String) table.getValueAt(row, 1);
                String desc = (String) table.getValueAt(row, 2);
                String date = (String) table.getValueAt(row, 3);

                try {
                    Project selectedProject = new Project(id, title, desc, date);
                    printAsProposal(selectedProject);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to print proposal: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a project to print as proposal.");
            }
        });
    }

    private void loadProjectData() throws Exception {
        try {
            List<Project> projects = projectService.getAllProjects();
            tableModel.setRowCount(0);
            for (Project p : projects) {
                tableModel.addRow(new Object[]{p.getId(), p.getTitle(), p.getDescription(), p.getDate()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load projects: " + e.getMessage());
        }
    }

    private void printAsProposal(Project project) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Proposal PDF");
            fileChooser.setSelectedFile(new File(project.getTitle().replaceAll("\\s+", "_") + "_Proposal.pdf"));
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection != JFileChooser.APPROVE_OPTION) return;

            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            com.itextpdf.text.Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            com.itextpdf.text.Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // Title
            Paragraph title = new Paragraph("PROJECT PROPOSAL", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Project Info
            document.add(new Paragraph("Project Title: " + project.getTitle(), normalFont));
            document.add(new Paragraph("Date: " + project.getDate(), normalFont));
            document.add(new Paragraph(" "));

            // Sections
            document.add(new Paragraph("I. Project Description", sectionFont));
            document.add(new Paragraph(project.getDescription(), normalFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("II. Objectives", sectionFont));
            document.add(new Paragraph("- To engage youth participation in community development.", normalFont));
            document.add(new Paragraph("- To promote leadership, camaraderie, and social responsibility.", normalFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("III. Expected Outcome", sectionFont));
            document.add(new Paragraph("The project is expected to benefit the youth and foster active involvement in SK activities.", normalFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("IV. Budget (if applicable)", sectionFont));
            document.add(new Paragraph("Amount: Php 0.00", normalFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("V. Approval", sectionFont));
            document.add(new Paragraph("\nPrepared by:", normalFont));
            document.add(new Paragraph("__________________________", normalFont));
            document.add(new Paragraph("SK Chairperson", normalFont));
            document.add(new Paragraph("\n\nApproved by:", normalFont));
            document.add(new Paragraph("__________________________", normalFont));
            document.add(new Paragraph("Barangay Captain", normalFont));

            document.close();
            JOptionPane.showMessageDialog(this, "Formal proposal PDF saved:\n" + filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to generate proposal PDF: " + ex.getMessage(),
                    "PDF Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
