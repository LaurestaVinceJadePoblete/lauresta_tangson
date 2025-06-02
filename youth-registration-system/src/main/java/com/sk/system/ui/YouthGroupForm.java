package com.sk.system.ui;

import com.sk.system.model.Group;
import com.sk.system.model.Member;
import com.sk.system.service.GroupService;
import com.sk.system.service.MemberService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YouthGroupForm extends JFrame {
    private final GroupService groupService = new GroupService();
    private final MemberService memberService = new MemberService();

    private final DefaultTableModel groupModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description"}, 0);
    private final DefaultTableModel memberModel = new DefaultTableModel(new Object[]{"ID", "Name"}, 0);
    private final DefaultTableModel groupMembersModel = new DefaultTableModel(new Object[]{"ID", "Name"}, 0);

    private JTable groupTable;
    private JTable memberTable;
    private JTable groupMembersTable;

    public YouthGroupForm() throws Exception {
        setTitle("Youth Organization Groups");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        loadGroups();
        loadMembers();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Group form inputs
        JPanel groupFormPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField groupNameField = new JTextField();
        JTextArea groupDescField = new JTextArea(3, 20);
        JButton addGroupBtn = new JButton("Add Group");
        JButton editGroupBtn = new JButton("Edit Selected Group");   // NEW
        JButton deleteGroupBtn = new JButton("Delete Selected Group"); // NEW

        groupFormPanel.setBorder(BorderFactory.createTitledBorder("Group Management"));
        groupFormPanel.add(new JLabel("Group Name:"));
        groupFormPanel.add(groupNameField);
        groupFormPanel.add(new JLabel("Description:"));
        groupFormPanel.add(new JScrollPane(groupDescField));
        groupFormPanel.add(addGroupBtn);
        groupFormPanel.add(editGroupBtn); // NEW
        groupFormPanel.add(deleteGroupBtn); // NEW

        // Tables
        groupTable = new JTable(groupModel);
        JScrollPane groupScroll = new JScrollPane(groupTable);
        groupScroll.setBorder(BorderFactory.createTitledBorder("Groups"));

        memberTable = new JTable(memberModel);
        JScrollPane memberScroll = new JScrollPane(memberTable);
        memberScroll.setBorder(BorderFactory.createTitledBorder("All Members"));

        groupMembersTable = new JTable(groupMembersModel);
        JScrollPane groupMembersScroll = new JScrollPane(groupMembersTable);
        groupMembersScroll.setBorder(BorderFactory.createTitledBorder("Members of Selected Group"));

        // Action buttons
        JButton assignBtn = new JButton("Assign Selected Member to Selected Group");
        JButton removeMemberBtn = new JButton("Remove Selected Member from Group");

        JPanel tablesPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        tablesPanel.add(groupScroll);
        tablesPanel.add(memberScroll);
        tablesPanel.add(groupMembersScroll);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonsPanel.add(assignBtn);
        buttonsPanel.add(removeMemberBtn);

        panel.add(groupFormPanel, BorderLayout.NORTH);
        panel.add(tablesPanel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        setContentPane(panel);

        // Load members of selected group
        groupTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = groupTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int groupId = (int) groupModel.getValueAt(selectedRow, 0);
                    try {
                        loadGroupMembers(groupId);
                        groupNameField.setText((String) groupModel.getValueAt(selectedRow, 1));
                        groupDescField.setText((String) groupModel.getValueAt(selectedRow, 2));
                    } catch (Exception ex) {
                        Logger.getLogger(YouthGroupForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        // Add group
        addGroupBtn.addActionListener(e -> {
            String name = groupNameField.getText().trim();
            String desc = groupDescField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Group name cannot be empty.");
                return;
            }

            try {
                groupService.addGroup(new Group(name, desc));
                loadGroups();
                groupNameField.setText("");
                groupDescField.setText("");
            } catch (Exception ex) {
                Logger.getLogger(YouthGroupForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Edit group (NEW)
        editGroupBtn.addActionListener(e -> {
            int selectedRow = groupTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a group to edit.");
                return;
            }

            int groupId = (int) groupModel.getValueAt(selectedRow, 0);
            String newName = groupNameField.getText().trim();
            String newDesc = groupDescField.getText().trim();

            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Group name cannot be empty.");
                return;
            }

            try {
                groupService.updateGroup(new Group(groupId, newName, newDesc));
                JOptionPane.showMessageDialog(this, "Group updated successfully.");
                loadGroups();
            } catch (Exception ex) {
                Logger.getLogger(YouthGroupForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Delete group (NEW)
        deleteGroupBtn.addActionListener(e -> {
            int selectedRow = groupTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a group to delete.");
                return;
            }

            int groupId = (int) groupModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this group?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    groupService.deleteGroup(groupId);
                    JOptionPane.showMessageDialog(this, "Group deleted successfully.");
                    loadGroups();
                    groupNameField.setText("");
                    groupDescField.setText("");
                    groupMembersModel.setRowCount(0);
                } catch (Exception ex) {
                    Logger.getLogger(YouthGroupForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Assign member to group
        assignBtn.addActionListener(e -> {
            int groupRow = groupTable.getSelectedRow();
            int memberRow = memberTable.getSelectedRow();

            if (groupRow < 0 || memberRow < 0) {
                JOptionPane.showMessageDialog(this, "Select both a group and a member.");
                return;
            }

            int groupId = (int) groupModel.getValueAt(groupRow, 0);
            int memberId = (int) memberModel.getValueAt(memberRow, 0);

            try {
                boolean assigned = groupService.assignMemberToGroup(groupId, memberId);
                if (assigned) {
                    JOptionPane.showMessageDialog(this, "Member assigned to group successfully.");
                    loadGroupMembers(groupId);
                } else {
                    JOptionPane.showMessageDialog(this, "Member is already in this group.");
                }
            } catch (Exception ex) {
                Logger.getLogger(YouthGroupForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Remove member from group
        removeMemberBtn.addActionListener(e -> {
            int groupRow = groupTable.getSelectedRow();
            int memberRow = groupMembersTable.getSelectedRow();

            if (groupRow < 0 || memberRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a group and a member from that group.");
                return;
            }

            int groupId = (int) groupModel.getValueAt(groupRow, 0);
            int memberId = (int) groupMembersModel.getValueAt(memberRow, 0);

            try {
                boolean removed = groupService.removeMemberFromGroup(groupId, memberId);
                if (removed) {
                    JOptionPane.showMessageDialog(this, "Member removed from group successfully.");
                    loadGroupMembers(groupId);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to remove member from group.");
                }
            } catch (Exception ex) {
                Logger.getLogger(YouthGroupForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void loadGroups() throws Exception {
        groupModel.setRowCount(0);
        List<Group> groups = groupService.getAllGroups();
        for (Group g : groups) {
            groupModel.addRow(new Object[]{g.getId(), g.getName(), g.getDescription()});
        }
    }

    private void loadMembers() {
        memberModel.setRowCount(0);
        List<Member> members = memberService.getAllMembers();
        for (Member m : members) {
            memberModel.addRow(new Object[]{m.getId(), m.getName()});
        }
    }

    private void loadGroupMembers(int groupId) throws Exception {
        groupMembersModel.setRowCount(0);
        List<Member> groupMembers = groupService.getGroupMembers(groupId);
        for (Member m : groupMembers) {
            groupMembersModel.addRow(new Object[]{m.getId(), m.getName()});
        }
    }
}
