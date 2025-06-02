package com.sk.system.ui;

import com.sk.system.database.DatabaseHelper;
import com.sk.system.model.Member;
import com.sk.system.model.Group;
import com.sk.system.service.MemberService;
import com.sk.system.service.GroupService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainApp extends JFrame {
    private final MemberService service = new MemberService();
    private final GroupService groupService = new GroupService();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Age", "Address", "Contact", "Gender", "Birthdate", "School"}, 0);
    private JTable table;
    private JTextField memberSearchField = new JTextField(20);
    private List<Member> allMembers = new ArrayList<>();

    // Form fields
    private JTextField nameField = new JTextField(15);
    private JTextField ageField = new JTextField(15);
    private JTextField addressField = new JTextField(15);
    private JTextField contactField = new JTextField(15);
    private JTextField genderField = new JTextField(15);
    private JTextField birthdateField = new JTextField(15);
    private JTextField schoolField = new JTextField(15);

    public MainApp() {
        setTitle("Youth Registration and Monitoring System - SK");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadMembers();
    }

    private void initUI() {
        // Left Panel: Form + buttons
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Member Info Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Member Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Name:", "Age:", "Address:", "Contact:", "Gender:", "Birthdate:", "School:"};
        JTextField[] fields = {nameField, ageField, addressField, contactField, genderField, birthdateField, schoolField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;
            formPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            formPanel.add(fields[i], gbc);
        }

        leftPanel.add(formPanel, BorderLayout.NORTH);

        // Member CRUD Buttons
        JPanel memberButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton addBtn = new JButton("Register Member");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton printBtn = new JButton("Print");
        memberButtonPanel.add(addBtn);
        memberButtonPanel.add(updateBtn);
        memberButtonPanel.add(deleteBtn);
        memberButtonPanel.add(printBtn);
        leftPanel.add(memberButtonPanel, BorderLayout.CENTER);

        // Other Functional Buttons Grouped in Tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Educational Assistance + Group
        JPanel assistanceGroupPanel = new JPanel(new GridLayout(10, 10, 10, 10));
        JButton assistanceBtn = new JButton("Add Student for Educational Assistance");
        JButton viewAssistanceBtn = new JButton("Educational Assistance Listing");
        JButton assignBtn = new JButton("Assign to Group");
        JButton printGroupBtn = new JButton("Print Group Assignments");
        assistanceGroupPanel.add(assistanceBtn);
        assistanceGroupPanel.add(viewAssistanceBtn);
        assistanceGroupPanel.add(assignBtn);
        assistanceGroupPanel.add(printGroupBtn);
        tabbedPane.addTab("Assistance & Groups", assistanceGroupPanel);

        // Announcements and Events
        JPanel announcementPanel = new JPanel(new GridLayout(10, 10, 10, 10));
        JButton announcementBtn = new JButton("Create Announcement");
        JButton viewAnnouncementsBtn = new JButton("See Announcements");
        JButton eventsBtn = new JButton("Manage Events");
        announcementPanel.add(announcementBtn);
        announcementPanel.add(viewAnnouncementsBtn);
        announcementPanel.add(eventsBtn);
        tabbedPane.addTab("Announcements & Events", announcementPanel);

        // Projects
        JPanel projectPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton projectBtn = new JButton("Manage Projects");
        projectPanel.add(projectBtn);
        tabbedPane.addTab("Projects", projectPanel);

        leftPanel.add(tabbedPane, BorderLayout.SOUTH);

        // Right Panel: Search + Table
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Members:"));
        searchPanel.add(memberSearchField);

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Registered Members"));

        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        // Split pane to separate left and right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.5);

        setContentPane(splitPane);

        // Action Listeners
        addBtn.addActionListener(e -> {
            try {
                Member m = new Member(
                        nameField.getText(),
                        Integer.parseInt(ageField.getText()),
                        addressField.getText(),
                        contactField.getText(),
                        genderField.getText(),
                        birthdateField.getText(),
                        schoolField.getText()
                );
                service.addMember(m);
                clearFields(fields);
                loadMembers();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for Age.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                try {
                    int id = (int) tableModel.getValueAt(row, 0);
                    Member m = new Member(
                            id,
                            nameField.getText(),
                            Integer.parseInt(ageField.getText()),
                            addressField.getText(),
                            contactField.getText(),
                            genderField.getText(),
                            birthdateField.getText(),
                            schoolField.getText()
                    );
                    service.updateMember(m);
                    loadMembers();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number for Age.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) tableModel.getValueAt(row, 0);
                service.deleteMember(id);
                loadMembers();
            }
        });

        printBtn.addActionListener(e -> {
            List<Member> members = service.getAllMembers();

            if (members.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No members to print.", "Print", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("List of Registered Youth Members\n");
            sb.append("=================================\n\n");
            for (Member m : members) {
                sb.append("ID: ").append(m.getId()).append("\n");
                sb.append("Name: ").append(m.getName()).append("\n");
                sb.append("Age: ").append(m.getAge()).append("\n");
                sb.append("Address: ").append(m.getAddress()).append("\n");
                sb.append("Contact: ").append(m.getContact()).append("\n");
                sb.append("Gender: ").append(m.getGender()).append("\n");
                sb.append("Birthdate: ").append(m.getBirthdate()).append("\n");
                sb.append("School: ").append(m.getSchool()).append("\n");
                sb.append("---------------------------------\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            try {
                boolean printed = textArea.print();
                if (printed) {
                    JOptionPane.showMessageDialog(this, "Printing Complete", "Print", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Printing Cancelled", "Print", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception pe) {
                JOptionPane.showMessageDialog(this, "Printing Failed: " + pe.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        assistanceBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int memberId = (int) tableModel.getValueAt(row, 0);
                try {
                    new EducationalAssistanceForm(DatabaseHelper.getConnection(), memberId).setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member from the table first.");
            }
        });

        viewAssistanceBtn.addActionListener(e -> {
            try {
                new EducationalAssistanceListForm().setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Failed to open assistance list: " + ex.getMessage());
            }
        });

        assignBtn.addActionListener(e -> {
            try {
                new YouthGroupForm().setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Failed to open group assignment form: " + ex.getMessage());
            }
        });

        printGroupBtn.addActionListener(e -> {
            try {
                JTextArea textArea = new JTextArea();
                textArea.append("Youth Group Assignments:\n\n");

                List<Group> groups = groupService.getAllGroups();
                for (Group g : groups) {
                    textArea.append("Group: " + g.getName() + "\n");
                    List<Member> members = groupService.getGroupMembers(g.getId());
                    for (Member m : members) {
                        textArea.append("  - " + m.getName() + "\n");
                    }
                    textArea.append("\n");
                }

                textArea.print();
            } catch (Exception ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Failed to print: " + ex.getMessage());
            }
        });

        announcementBtn.addActionListener(e -> new AnnouncementForm().setVisible(true));
        viewAnnouncementsBtn.addActionListener(e -> new AnnouncementListForm().setVisible(true));
        eventsBtn.addActionListener(e -> {
            try {
                new EventListForm().setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        projectBtn.addActionListener(e -> {
            try {
                new ProjectForm().setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                nameField.setText((String) tableModel.getValueAt(row, 1));
                ageField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                addressField.setText((String) tableModel.getValueAt(row, 3));
                contactField.setText((String) tableModel.getValueAt(row, 4));
                genderField.setText((String) tableModel.getValueAt(row, 5));
                birthdateField.setText((String) tableModel.getValueAt(row, 6));
                schoolField.setText((String) tableModel.getValueAt(row, 7));
            }
        });

        memberSearchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterMembers(memberSearchField.getText());
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterMembers(memberSearchField.getText());
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterMembers(memberSearchField.getText());
            }
        });
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void loadMembers() {
        allMembers = service.getAllMembers();
        tableModel.setRowCount(0);
        for (Member m : allMembers) {
            tableModel.addRow(new Object[]{
                    m.getId(),
                    m.getName(),
                    m.getAge(),
                    m.getAddress(),
                    m.getContact(),
                    m.getGender(),
                    m.getBirthdate(),
                    m.getSchool()
            });
        }
    }

    private void filterMembers(String query) {
        tableModel.setRowCount(0);
        String lowerQuery = query.toLowerCase();

        for (Member m : allMembers) {
            if (m.getName().toLowerCase().contains(lowerQuery)) {
                tableModel.addRow(new Object[]{
                        m.getId(),
                        m.getName(),
                        m.getAge(),
                        m.getAddress(),
                        m.getContact(),
                        m.getGender(),
                        m.getBirthdate(),
                        m.getSchool()
                });
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}
