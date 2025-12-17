package ui;

import dao.UserDAO;
import service.LoggedUser;
import service.LoginService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class LoginWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private final LoginService loginService =
            new LoginService(new UserDAO());

    public LoginWindow() {
        setTitle("Al Rahhala - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
        layoutComponents();
        registerListeners();
    }

    private void initComponents() {
        usernameField = new JTextField(18);
        passwordField = new JPasswordField(18);

        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(140, 36));
        getRootPane().setDefaultButton(loginButton);
    }

    private void layoutComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(18, 18, 18, 18));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Al Rahhala Shipping System");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        JLabel subtitle = new JLabel("Sign in to continue");
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 12f));

        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);

        header.add(titleBox, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(18, 0, 10, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Username"), gbc);

        gbc.gridy = 1;
        form.add(usernameField, gbc);

        gbc.gridy = 2;
        form.add(new JLabel("Password"), gbc);

        gbc.gridy = 3;
        form.add(passwordField, gbc);

        root.add(form, BorderLayout.CENTER);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.add(loginButton);

        root.add(actions, BorderLayout.SOUTH);

        add(root);
    }

    private void registerListeners() {
        loginButton.addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            LoggedUser user = loginService.login(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (user.isAdmin()) {
                MainWindow mainWindow = new MainWindow(user);
                mainWindow.setVisible(true);
            } else {
                ShipmentMenuWindow shipmentMenuWindow = new ShipmentMenuWindow(user);
                shipmentMenuWindow.setVisible(true);
            }

            dispose();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Database error",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
