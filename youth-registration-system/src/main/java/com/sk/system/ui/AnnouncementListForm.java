package com.sk.system.ui;

import com.sk.system.model.Announcement;
import com.sk.system.service.AnnouncementService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class AnnouncementListForm extends JFrame {
    private final AnnouncementService service = new AnnouncementService();
    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Content"}, 0);
    private JTable table;

    public AnnouncementListForm() {
        setTitle("📢 Announcements");
        setSize(700, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Debug image loading
        debugImageLoading();
        
        initUI();
        loadAnnouncements();
    }

    private void initUI() {
        // Table setup
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        // Alternate row color renderer
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                } else {
                    c.setBackground(new Color(200, 230, 255));
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons
        JButton deleteBtn = new JButton("🗑 Delete");
        JButton printBtn = new JButton("🖨 Print");

        styleButton(deleteBtn, new Color(220, 53, 69));
        styleButton(printBtn, new Color(40, 167, 69));

        deleteBtn.addActionListener(e -> deleteSelectedAnnouncement());
        printBtn.addActionListener(e -> printSelectedAnnouncement());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPanel.add(printBtn);
        buttonPanel.add(deleteBtn);

        setLayout(new BorderLayout(10, 10));
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 35));
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
    }

    private void loadAnnouncements() {
        tableModel.setRowCount(0);
        List<Announcement> announcements = service.getAllAnnouncements();
        for (Announcement a : announcements) {
            tableModel.addRow(new Object[]{a.getId(), a.getTitle(), a.getContent()});
        }
    }

    private void deleteSelectedAnnouncement() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this announcement?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                service.deleteAnnouncement(id);
                loadAnnouncements();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an announcement to delete.");
        }
    }

    private void printSelectedAnnouncement() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            String content = (String) tableModel.getValueAt(selectedRow, 2);
            printAnnouncement(title, content);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an announcement to print.");
        }
    }

    private void printAnnouncement(String title, String content) {
        JTextPane printPane = new JTextPane();
        printPane.setContentType("text/html");

        // Optionally add CSS styling to the JTextPane
        HTMLEditorKit editorKit = new HTMLEditorKit();
        printPane.setEditorKit(editorKit);
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.addRule("body {font-family: Serif; font-size: 12pt;}");
        editorKit.setStyleSheet(styleSheet);

        // Load images from same package (relative path)
        URL leftLogoURL = getClass().getResource("p1.png");
        URL rightLogoURL = getClass().getResource("p2.png");

        System.out.println("Left Logo URL: " + leftLogoURL);
        System.out.println("Right Logo URL: " + rightLogoURL);

        String leftImageTag = "";
        if (leftLogoURL != null) {
            leftImageTag = "<img src='" + leftLogoURL + "' height='80'/>";
        } else {
            System.err.println("Error: Left logo image not found");
            leftImageTag = "<div style='width:80px; height:80px; background-color:#ccc; display:inline-block; text-align:center; line-height:80px; border:1px solid #999;'>Logo 1</div>";
        }

        String rightImageTag = "";
        if (rightLogoURL != null) {
            rightImageTag = "<img src='" + rightLogoURL + "' height='80'/>";
        } else {
            System.err.println("Error: Right logo image not found");
            rightImageTag = "<div style='width:80px; height:80px; background-color:#ccc; display:inline-block; text-align:center; line-height:80px; border:1px solid #999;'>Logo 2</div>";
        }

        String html = "<html>" +
                "<body>" +
                "<table width='100%'>" +
                "<tr>" +
                "<td width='20%' align='left'>" + leftImageTag + "</td>" +
                "<td width='60%' align='center'>" +
                "<h3>Republic of the Philippines</h3>" +
                "<h3>Province of Aurora</h3>" +
                "<h3>Municipality of San Luis</h3>" +
                "<h3>Barangay San Jose</h3>" +
                "<h3>OFFICE OF THE SANGGUNIANG KABATAAN</h3>" +
                "</td>" +
                "<td width='20%' align='right'>" + rightImageTag + "</td>" +
                "</tr>" +
                "</table>" +
                "<br><br>" +
                "<div style='font-size: 14px;'>" +
                "<strong>📢 Title:</strong> " + title + "<br><br>" +
                "<strong>📝 Content:</strong><br>" +
                content.replaceAll("\n", "<br>") +
                "</div>" +
                "</body>" +
                "</html>";

        printPane.setText(html);
        printPane.setEditable(false);

        try {
            boolean complete = printPane.print();
            if (complete) {
                JOptionPane.showMessageDialog(this, "✅ Printing complete.");
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Printing canceled.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Failed to print: " + ex.getMessage());
        }
    }

    // Simple debug method to verify image loading
    private void debugImageLoading() {
        System.out.println("=== Image Loading Debug ===");
        System.out.println("Class package: " + getClass().getPackage().getName());
        
        URL p1 = getClass().getResource("p1.png");
        URL p2 = getClass().getResource("p2.png");
        
        System.out.println("p1.png found: " + (p1 != null ? p1.toString() : "NOT FOUND"));
        System.out.println("p2.png found: " + (p2 != null ? p2.toString() : "NOT FOUND"));
        System.out.println("=========================");
    }
}

/*
RESOURCE PLACEMENT CONFIRMED:

Based on your directory structure, your images are correctly placed in:
- com.sk.system.ui/p1.png 
- com.sk.system.ui/p2.png

The code now uses relative path loading: getClass().getResource("p1.png")

This should work since the images are in the same package as AnnouncementListForm.java

If you still have issues:
1. Make sure your IDE copies .png files to the output directory
2. Check that the images are included in your JAR file (if building a JAR)
3. Run the application and check the console debug output

The debugImageLoading() method will tell you if the images are found or not.
*/