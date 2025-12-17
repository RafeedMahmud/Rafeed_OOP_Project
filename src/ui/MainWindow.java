package ui;

import service.LoggedUser;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final LoggedUser loggedUser;

    public MainWindow(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;

        setTitle("Al Rahhala - Main Window");
        setSize(900, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // NEW: زر واحد للشحنات يفتح صفحة تنظيمية
        JButton shipmentsButton = new JButton("Shipments");
        shipmentsButton.addActionListener(e -> new ShipmentMenuWindow(loggedUser).setVisible(true));

        JButton dashboardButton = new JButton("Dashboard");
        dashboardButton.addActionListener(e -> new DashboardWindow().setVisible(true));

        JButton reportsButton = new JButton("Reports");
        reportsButton.addActionListener(e -> new ReportWindow().setVisible(true));

        // User Settings (Admin only)
        JButton userSettingsButton = new JButton("User Settings");
        userSettingsButton.addActionListener(e -> new UserManagementWindow().setVisible(true));

        JButton logoutButton = new JButton("Logout");
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

        // الصلاحيات: المدير فقط
        if (!loggedUser.isAdmin()) {
            dashboardButton.setEnabled(false);
            reportsButton.setEnabled(false);
            userSettingsButton.setEnabled(false);
        }

        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
        add(shipmentsButton);
        add(dashboardButton);
        add(reportsButton);
        add(userSettingsButton);
        add(logoutButton);
    }
}
