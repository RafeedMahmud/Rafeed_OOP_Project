package ui;

import service.DashboardService;
import service.ServiceFactory;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class DashboardWindow extends JFrame {

    private JLabel totalLabel;
    private JLabel registeredLabel;
    private JLabel updatedLabel;
    private JLabel todayLabel;

    private final DashboardService dashboardService =
            ServiceFactory.getDashboardService();

    public DashboardWindow() {
        setTitle("Dashboard");
        setSize(420, 260);
        setLocationRelativeTo(null);

        initComponents();
        loadStats();
    }

    private void initComponents() {
        totalLabel = new JLabel();
        registeredLabel = new JLabel();
        updatedLabel = new JLabel();
        todayLabel = new JLabel();

        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(totalLabel);
        panel.add(registeredLabel);
        panel.add(updatedLabel);
        panel.add(todayLabel);

        add(panel);
    }

    private void loadStats() {
        try {
            totalLabel.setText("Total Shipments: " + dashboardService.getTotalShipments());
            registeredLabel.setText("Registered Shipments: " + dashboardService.getRegisteredShipments());
            updatedLabel.setText("Updated Shipments: " + dashboardService.getUpdatedShipments());
            todayLabel.setText("Shipments Today: " + dashboardService.getTodayShipments());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
