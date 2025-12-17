package ui;

import service.ServiceFactory;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class AddUserWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton saveButton;

    private final UserService userService = ServiceFactory.getUserService();

    public AddUserWindow() {
        setTitle("Add User (Admin)");
        setSize(560, 360);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        registerListeners();
    }

    private void initComponents() {
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        usernameField = new JTextField(18);
        usernameField.setFont(fieldFont);

        passwordField = new JPasswordField(18);
        passwordField.setFont(fieldFont);

        roleCombo = new JComboBox<>();
        roleCombo.addItem("ADMIN");
        roleCombo.addItem("EMPLOYEE");
        roleCombo.setFont(fieldFont);

        saveButton = new JButton("Save User");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveButton.setPreferredSize(new Dimension(160, 38));
    }

    private void layoutComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout(12, 12));
        rootPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        rootPanel.setBackground(new Color(245, 246, 250));

        JLabel titleLabel = new JLabel("Add New User", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(5, 5, 10, 5));
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Username
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel uLabel = new JLabel("Username:");
        uLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(uLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(usernameField, gbc);

        // Row 1: Password
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel pLabel = new JLabel("Password:");
        pLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(pLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(passwordField, gbc);

        // Row 2: Role
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel rLabel = new JLabel("Role:");
        rLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(rLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(roleCombo, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonPanel.add(saveButton);

        cardPanel.add(formPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        rootPanel.add(cardPanel, BorderLayout.CENTER);

        setContentPane(rootPanel);
    }

    private void registerListeners() {
        saveButton.addActionListener(e -> doSave());
    }

    private void doSave() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        try {
            boolean ok = userService.addUser(username, password, role);

            if (!ok) {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add user.\n- Username may already exist\n- Or invalid input",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "User added successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            usernameField.setText("");
            passwordField.setText("");
            roleCombo.setSelectedIndex(0);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Database error:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
 