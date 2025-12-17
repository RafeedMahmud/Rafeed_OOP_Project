package ui;

import service.ServiceFactory;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        setSize(860, 520);
        setLocationRelativeTo(null);

        initComponents();
        buildTabs();
    }

    private void initComponents() {
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Add
        addUsername = new JTextField(18);
        addPassword = new JPasswordField(18);
        addRole = new JComboBox<>(new String[]{"ADMIN", "EMPLOYEE"});
        addSaveBtn = new JButton("Add User");

        addUsername.setFont(fieldFont);
        addPassword.setFont(fieldFont);
        addRole.setFont(fieldFont);
        addSaveBtn.setFont(btnFont);

        addSaveBtn.addActionListener(e -> doAddUser());

        // Delete
        delUsername = new JTextField(18);
        delBtn = new JButton("Delete User");

        delUsername.setFont(fieldFont);
        delBtn.setFont(btnFont);

        delBtn.addActionListener(e -> doDeleteUser());

        // Update
        updUsername = new JTextField(18);
        updNewPassword = new JPasswordField(18);
        updRole = new JComboBox<>(new String[]{"ADMIN", "EMPLOYEE"});
        updPassBtn = new JButton("Change Password");
        updRoleBtn = new JButton("Change Role");

        updUsername.setFont(fieldFont);
        updNewPassword.setFont(fieldFont);
        updRole.setFont(fieldFont);
        updPassBtn.setFont(btnFont);
        updRoleBtn.setFont(btnFont);

        updPassBtn.addActionListener(e -> doChangePassword());
        updRoleBtn.addActionListener(e -> doChangeRole());

        // Search
        searchUsername = new JTextField(18);
        searchBtn = new JButton("Search");
        searchResult = new JTextArea(6, 40);
        searchResult.setEditable(false);
        searchResult.setFont(new Font("Consolas", Font.PLAIN, 13));
        searchResult.setBorder(new EmptyBorder(8, 8, 8, 8));

        searchUsername.setFont(fieldFont);
        searchBtn.setFont(btnFont);

        searchBtn.addActionListener(e -> doSearch());

        // List
        usersTable = new JTable();
        usersTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        usersTable.setRowHeight(26);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        usersTable.setFillsViewportHeight(true);

        JTableHeader header = usersTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setReorderingAllowed(false);

        refreshBtn = new JButton("Refresh List");
        refreshBtn.setFont(btnFont);
        refreshBtn.addActionListener(e -> loadUsers());
    }

    private void buildTabs() {
        tabs.addTab("Add User", wrap(buildAddPanel()));
        tabs.addTab("Delete User", wrap(buildDeletePanel()));
        tabs.addTab("Update User", wrap(buildUpdatePanel()));
        tabs.addTab("Search", wrap(buildSearchPanel()));
        tabs.addTab("All Users", wrap(buildListPanel()));

        add(tabs, BorderLayout.CENTER);

        // Load list once
        loadUsers();
    }

    private JPanel wrap(JPanel content) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(15, 15, 15, 15));
        p.add(content, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildAddPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = baseGbc();

        addRow(p, gbc, 0, "Username:", addUsername);
        addRow(p, gbc, 1, "Password:", addPassword);
        addRow(p, gbc, 2, "Role:", addRole);

        gbc.gridy = 3;
        gbc.gridx = 1;
        gbc.weightx = 0;
        p.add(addSaveBtn, gbc);

        return p;
    }

    private JPanel buildDeletePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = baseGbc();

        addRow(p, gbc, 0, "Username:", delUsername);

        gbc.gridy = 1;
        gbc.gridx = 1;
        p.add(delBtn, gbc);

        return p;
    }

    private JPanel buildUpdatePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = baseGbc();

        addRow(p, gbc, 0, "Username:", updUsername);
        addRow(p, gbc, 1, "New Password:", updNewPassword);

        gbc.gridy = 2;
        gbc.gridx = 1;
        p.add(updPassBtn, gbc);

        addRow(p, gbc, 3, "New Role:", updRole);

        gbc.gridy = 4;
        gbc.gridx = 1;
        p.add(updRoleBtn, gbc);

        return p;
    }

    private JPanel buildSearchPanel() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        top.add(new JLabel("Username:"));
        top.add(searchUsername);
        top.add(searchBtn);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(new EmptyBorder(10, 0, 0, 0));
        center.add(new JScrollPane(searchResult), BorderLayout.CENTER);

        JPanel main = new JPanel(new BorderLayout());
        main.add(top, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);

        return main;
    }

    private JPanel buildListPanel() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.add(refreshBtn);

        JPanel main = new JPanel(new BorderLayout(8, 8));
        main.add(top, BorderLayout.NORTH);
        main.add(new JScrollPane(usersTable), BorderLayout.CENTER);

        return main;
    }

    private GridBagConstraints baseGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        return gbc;
    }

    private void addRow(JPanel p, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        p.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        p.add(field, gbc);
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

            JTableHeader header = usersTable.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 13));
            header.setReorderingAllowed(false);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error:\n" + ex.getMessage());
        }
    }
}
 