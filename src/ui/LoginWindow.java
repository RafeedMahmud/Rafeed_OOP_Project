package ui;

import dao.UserDAO;
import service.LoggedUser;
import service.LoginService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private final LoginService loginService =
            new LoginService(new UserDAO());

    public LoginWindow() {
        setTitle("Login - Al Rahhala");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        layoutComponents();
        registerListeners();
    }

    private void initComponents() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
    }

    private void layoutComponents() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel, BorderLayout.CENTER);
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

            // فتح MainWindow بعد النجاح
            MainWindow mainWindow = new MainWindow(user);
            mainWindow.setVisible(true);
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
