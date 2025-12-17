package ui;

import service.LoggedUser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainWindow extends JFrame {

    private final LoggedUser loggedUser;

    public MainWindow(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;

        setTitle("Al Rahhala - Main Window");
        setSize(900, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== Main Container =====
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        rootPanel.setBackground(new Color(245, 246, 250));

        // ===== Title =====
        JLabel titleLabel = new JLabel("Al Rahhala Shipping System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(10, 10, 20, 10));
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        // ===== Card Panel =====
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        Dimension buttonSize = new Dimension(200, 45);
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);

        // ===== Buttons =====
        JButton shipmentsButton = new JButton("Shipments");
        shipmentsButton.setPreferredSize(buttonSize);
        shipmentsButton.setFont(buttonFont);
        shipmentsButton.addActionListener(e ->
                new ShipmentMenuWindow(loggedUser).setVisible(true)
        );

        JButton dashboardButton = new JButton("Dashboard");
        dashboardButton.setPreferredSize(buttonSize);
        dashboardButton.setFont(buttonFont);
        dashboardButton.addActionListener(e ->
                new DashboardWindow().setVisible(true)
        );

        JButton reportsButton = new JButton("Reports");
        reportsButton.setPreferredSize(buttonSize);
        reportsButton.setFont(buttonFont);
        reportsButton.addActionListener(e ->
                new ReportWindow().setVisible(true)
        );

        JButton userSettingsButton = new JButton("User Settings");
        userSettingsButton.setPreferredSize(buttonSize);
        userSettingsButton.setFont(buttonFont);
        userSettingsButton.addActionListener(e ->
                new UserManagementWindow().setVisible(true)
        );

        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(buttonSize);
        logoutButton.setFont(buttonFont);
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new LoginWindow().setVisible(true);
            }
        });

        // ===== Permissions (Admin only) =====
        if (!loggedUser.isAdmin()) {
            dashboardButton.setEnabled(false);
            reportsButton.setEnabled(false);
            userSettingsButton.setEnabled(false);
        }

        // ===== Add Buttons to Card =====
        cardPanel.add(shipmentsButton, gbc);
        gbc.gridx++;
        cardPanel.add(dashboardButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        cardPanel.add(reportsButton, gbc);
        gbc.gridx++;
        cardPanel.add(userSettingsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        cardPanel.add(logoutButton, gbc);

        rootPanel.add(cardPanel, BorderLayout.CENTER);

        setContentPane(rootPanel);
    }
}
