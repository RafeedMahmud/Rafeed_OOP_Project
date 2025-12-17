package ui;

import service.ServiceFactory;
import service.UserService;

import javax.swing.*;
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
        setSize(420, 220);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        registerListeners();
    }

    private void initComponents() {
        usernameField = new JTextField(18);
        passwordField = new JPasswordField(18);

        roleCombo = new JComboBox<>();
        roleCombo.addItem("ADMIN");
        roleCombo.addItem("EMPLOYEE");

        saveButton = new JButton("Save User");
    }

    private void layoutComponents() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        panel.add(roleCombo);

        panel.add(new JLabel());
        panel.add(saveButton);

        add(panel);
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
