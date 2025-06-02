package com.sk.system.ui;

import com.sk.system.model.EducationalAssistance;
import com.sk.system.model.Member;
import com.sk.system.service.EducationalAssistanceService;
import com.sk.system.service.MemberService;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.List;

public class EducationalAssistanceListForm extends JFrame {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private EducationalAssistanceService assistanceService;
    private final MemberService memberService = new MemberService();

    public EducationalAssistanceListForm() throws SQLException {
        setTitle("Educational Assistance Records");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel(new Object[]{
                "Assistance ID", "Member Name", "School Name", "Level", "Type", "Date", "Amount", "Remarks"
        }, 0);

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton printButton = new JButton("Print");
        JButton removeButton = new JButton("Remove");
        JButton releaseButton = new JButton("Release");
        JButton requirementsButton = new JButton("Requirements");

        buttonPanel.add(printButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(releaseButton);
        buttonPanel.add(requirementsButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        printButton.addActionListener(e -> printSelectedRecord());
        removeButton.addActionListener(e -> deleteSelectedRecord());
        releaseButton.addActionListener(e -> generatePDF());
        requirementsButton.addActionListener(e -> printRequirementsPDF());

        try {
            assistanceService = new EducationalAssistanceService();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to initialize Educational Assistance Service: " + e.getMessage(),
                    "Initialization Error", JOptionPane.ERROR_MESSAGE);
            assistanceService = null;
        }

        if (assistanceService != null) {
            loadAssistanceRecords();
        }
    }

    private void loadAssistanceRecords() throws SQLException {
        List<EducationalAssistance> records = assistanceService.getAllAssistance();
        tableModel.setRowCount(0); // Clear table

        for (EducationalAssistance ea : records) {
            Member member = memberService.getMemberById(ea.getMemberId());
            String memberName = member != null ? member.getName() : "Unknown";

            tableModel.addRow(new Object[]{
                    ea.getId(),
                    memberName,
                    ea.getSchoolName(),
                    ea.getEducationLevel(),
                    ea.getAssistanceType(),
                    ea.getDateGiven(),
                    ea.getAmount(),
                    ea.getRemarks()
            });
        }
    }

    private void printSelectedRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            StringBuilder sb = new StringBuilder("Educational Assistance Details:\n\n");
            for (int i = 0; i < table.getColumnCount(); i++) {
                sb.append(table.getColumnName(i)).append(": ")
                        .append(table.getValueAt(selectedRow, i)).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Print Preview", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a record to print.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this record?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int assistanceId = (int) table.getValueAt(selectedRow, 0);
                try {
                    assistanceService.deleteAssistance(assistanceId);
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Record deleted successfully.");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void generatePDF() {
        try {
            List<EducationalAssistance> records = assistanceService.getAllAssistance();
            if (records.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No educational assistance records to export.");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save PDF");
            fileChooser.setSelectedFile(new java.io.File("EducationalAssistanceReport.pdf"));
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection != JFileChooser.APPROVE_OPTION) return;

            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Generate PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Educational Assistance Report"));
            document.add(new Paragraph("Generated on: " + java.time.LocalDate.now()));
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(7);
            pdfTable.setWidthPercentage(100);
            pdfTable.addCell("Member Name");
            pdfTable.addCell("School Name");
            pdfTable.addCell("Level");
            pdfTable.addCell("Type");
            pdfTable.addCell("Date");
            pdfTable.addCell("Amount");
            pdfTable.addCell("Remarks");

            for (EducationalAssistance ea : records) {
                Member member = memberService.getMemberById(ea.getMemberId());
                String memberName = (member != null) ? member.getName() : "Unknown";

                pdfTable.addCell(memberName);
                pdfTable.addCell(ea.getSchoolName());
                pdfTable.addCell(ea.getEducationLevel());
                pdfTable.addCell(ea.getAssistanceType());
                pdfTable.addCell(ea.getDateGiven().toString());
                pdfTable.addCell(String.valueOf(ea.getAmount()));
                pdfTable.addCell(ea.getRemarks());
            }

            document.add(pdfTable);
            document.close();

            // After PDF generation: delete all records from the database
            for (EducationalAssistance ea : records) {
                assistanceService.deleteAssistance(ea.getId());
            }

            loadAssistanceRecords();

            JOptionPane.showMessageDialog(this, "PDF generated and records released successfully.\nFile saved at:\n" + filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during release process: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void printRequirementsPDF() {
        try {
            File tempFile = File.createTempFile("educational_assistance_requirements_", ".pdf");
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(tempFile));
            document.open();

            // Fonts (fully qualified to avoid ambiguity)
            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font bodyFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.NORMAL);

            // Header - Formal Government Format
            Paragraph header = new Paragraph("Republic of the Philippines\nBarangay Youth Council\nEducational Assistance Program", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(Chunk.NEWLINE);

            // Title
            Paragraph title = new Paragraph("List of Documentary Requirements", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Instruction
            Paragraph instruction = new Paragraph("To qualify for the educational assistance program, applicants must prepare and submit the following required documents:", bodyFont);
            instruction.setSpacingAfter(10f);
            document.add(instruction);

            // Bullet List of Requirements
            com.itextpdf.text.List reqList = new com.itextpdf.text.List(com.itextpdf.text.List.ORDERED, 20);
            reqList.setIndentationLeft(30f);
            reqList.add(new ListItem("Barangay Clearance", bodyFont));
            reqList.add(new ListItem("Photocopy of Valid School ID", bodyFont));
            reqList.add(new ListItem("Certificate of Indigency", bodyFont));
            reqList.add(new ListItem("Latest Report Card or Grade Transcript", bodyFont));
            reqList.add(new ListItem("Formal Letter of Request or Application", bodyFont));
            document.add(reqList);

            // Optional footer
            document.add(Chunk.NEWLINE);
            Paragraph footer = new Paragraph("Issued by the Barangay Youth Council\nDate: " + java.time.LocalDate.now(), bodyFont);
            footer.setSpacingBefore(20f);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);

            document.close();

            // Print the document
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().print(tempFile);
            } else {
                JOptionPane.showMessageDialog(this, "Auto-printing is not supported on this system.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error printing requirements: " + ex.getMessage());
        }
    }
}
