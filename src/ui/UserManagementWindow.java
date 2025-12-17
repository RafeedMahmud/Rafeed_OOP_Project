package ui;

import service.ServiceFactory;
import service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UserManagementWindow extends JFrame {

    private final UserService userService = ServiceFactory.getUserService();

    // Tabs
    private JTabbedPane tabs;

    // Add User
    private JTextField addUsername;
    private JPasswordField addPassword;
    private JComboBox<String> addRole;
    private JButton addSaveBtn;

    // Delete User
    private JTextField delUsername;
    private JButton delBtn;

    // Update User
    private JTextField updUsername;
    private JPasswordField updNewPassword;
    private JComboBox<String> updRole;
    private JButton updPassBtn;
    private JButton updRoleBtn;

    // Search
    private JTextField searchUsername;
    private JButton searchBtn;
    private JTextArea searchResult;

    // List Users
    private JTable usersTable;
    private JButton refreshBtn;

    public UserManagementWindow() {
        setTitle("User Settings (Admin)");
        setSize(720, 420);
        setLocationRelativeTo(null);

        initComponents();
        buildTabs();
    }

    private void initComponents() {
        tabs = new JTabbedPane();

        // Add
        addUsername = new JTextField(18);
        addPassword = new JPasswordField(18);
        addRole = new JComboBox<>(new String[]{"ADMIN", "EMPLOYEE"});
        addSaveBtn = new JButton("Add User");
        addSaveBtn.addActionListener(e -> doAddUser());

        // Delete
        delUsername = new JTextField(18);
        delBtn = new JButton("Delete User");
        delBtn.addActionListener(e -> doDeleteUser());

        // Update
        updUsername = new JTextField(18);
        updNewPassword = new JPasswordField(18);
        updRole = new JComboBox<>(new String[]{"ADMIN", "EMPLOYEE"});
        updPassBtn = new JButton("Change Password");
        updRoleBtn = new JButton("Change Role");
        updPassBtn.addActionListener(e -> doChangePassword());
        updRoleBtn.addActionListener(e -> doChangeRole());

        // Search
        searchUsername = new JTextField(18);
        searchBtn = new JButton("Search");
        searchResult = new JTextArea(6, 40);
        searchResult.setEditable(false);
        searchBtn.addActionListener(e -> doSearch());

        // List
        usersTable = new JTable();
        refreshBtn = new JButton("Refresh List");
        refreshBtn.addActionListener(e -> loadUsers());
    }

    private void buildTabs() {
        tabs.addTab("Add User", buildAddPanel());
        tabs.addTab("Delete User", buildDeletePanel());
        tabs.addTab("Update User", buildUpdatePanel());
        tabs.addTab("Search", buildSearchPanel());
        tabs.addTab("All Users", buildListPanel());

        add(tabs, BorderLayout.CENTER);

        // Load list once
        loadUsers();
    }

    private JPanel buildAddPanel() {
        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        p.add(new JLabel("Username:"));
        p.add(addUsername);

        p.add(new JLabel("Password:"));
        p.add(addPassword);

        p.add(new JLabel("Role:"));
        p.add(addRole);

        p.add(new JLabel());
        p.add(addSaveBtn);

        return p;
    }

    private JPanel buildDeletePanel() {
        JPanel p = new JPanel(new GridLayout(2, 2, 8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        p.add(new JLabel("Username:"));
        p.add(delUsername);

        p.add(new JLabel());
        p.add(delBtn);

        return p;
    }

    private JPanel buildUpdatePanel() {
        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        p.add(new JLabel("Username:"));
        p.add(updUsername);

        p.add(new JLabel("New Password:"));
        p.add(updNewPassword);

        p.add(new JLabel());
        p.add(updPassBtn);

        p.add(new JLabel("New Role:"));
        p.add(updRole);

        // زر تغيير role يكون تحت بشكل أوضح
        JPanel wrapper = new JPanel(new BorderLayout(8, 8));
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        wrapper.add(p, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(updRoleBtn);
        wrapper.add(bottom, BorderLayout.SOUTH);

        return wrapper;
    }

    private JPanel buildSearchPanel() {
        JPanel top = new JPanel();
        top.add(new JLabel("Username:"));
        top.add(searchUsername);
        top.add(searchBtn);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        center.add(new JScrollPane(searchResult), BorderLayout.CENTER);

        JPanel main = new JPanel(new BorderLayout());
        main.add(top, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);

        return main;
    }

    private JPanel buildListPanel() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.add(refreshBtn);

        JPanel main = new JPanel(new BorderLayout());
        main.add(top, BorderLayout.NORTH);
        main.add(new JScrollPane(usersTable), BorderLayout.CENTER);

        return main;
    }

    private void doAddUser() {
        try {
            boolean ok = userService.addUser(
                    addUsername.getText(),
                    new String(addPassword.getPassword()),
                    (String) addRole.getSelectedItem()
            );

            if (!ok) {
                JOptionPane.showMessageDialog(this, "Failed to add user (invalid input or username exists).");
                return;
            }

            JOptionPane.showMessageDialog(this, "User added successfully.");
            addUsername.setText("");
            addPassword.setText("");
            addRole.setSelectedIndex(0);

            loadUsers();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error:\n" + ex.getMessage());
        }
    }

    private void doDeleteUser() {
        String u = delUsername.getText();

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this user?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) return;

        try {
            boolean ok = userService.deleteUser(u);

            if (!ok) {
                JOptionPane.showMessageDialog(this, "Failed to delete user (not found or protected user).");
                return;
            }

            JOptionPane.showMessageDialog(this, "User deleted successfully.");
            delUsername.setText("");

            loadUsers();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error:\n" + ex.getMessage());
        }
    }

    private void doChangePassword() {
        try {
            boolean ok = userService.changePassword(
                    updUsername.getText(),
                    new String(updNewPassword.getPassword())
            );

            if (!ok) {
                JOptionPane.showMessageDialog(this, "Failed to change password (check username/new password).");
                return;
            }

            JOptionPane.showMessageDialog(this, "Password updated successfully.");
            updNewPassword.setText("");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error:\n" + ex.getMessage());
        }
    }

    private void doChangeRole() {
        try {
            boolean ok = userService.changeRole(
                    updUsername.getText(),
                    (String) updRole.getSelectedItem()
            );

            if (!ok) {
                JOptionPane.showMessageDialog(this, "Failed to change role (check username or protected user).");
                return;
            }

            JOptionPane.showMessageDialog(this, "Role updated successfully.");
            loadUsers();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error:\n" + ex.getMessage());
        }
    }

    private void doSearch() {
        try {
            String[] user = userService.findUser(searchUsername.getText());

            if (user == null) {
                searchResult.setText("User not found.");
                return;
            }

            searchResult.setText(
                    "Username: " + user[0] + "\n" +
                    "Role: " + user[1]
            );

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error:\n" + ex.getMessage());
        }
    }

    private void loadUsers() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Username", "Role"}, 0);

        try {
            List<String[]> rows = userService.listUsers();
            for (String[] r : rows) {
                model.addRow(new Object[]{r[0], r[1]});
            }
            usersTable.setModel(model);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error:\n" + ex.getMessage());
        }
    }
}
