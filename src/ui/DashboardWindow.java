package ui;

import dao.ShipmentDAO;
import service.DashboardService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class DashboardWindow extends JFrame {

    private JLabel totalLabel;
    private JLabel registeredLabel;
    private JLabel updatedLabel;
    private JLabel todayLabel;

    private JButton refreshButton;

    private final DashboardService dashboardService =
            new DashboardService(new ShipmentDAO());

    public DashboardWindow() {
        setTitle("Dashboard - Statistics");
        setSize(420, 260);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        registerListeners();

        loadStats();
    }

    private void initComponents() {
        totalLabel = new JLabel("Total Shipments: -");
        registeredLabel = new JLabel("Registered Shipments: -");
        updatedLabel = new JLabel("Updated Shipments: -");
        todayLabel = new JLabel("Shipments Today: -");

        refreshButton = new JButton("Refresh");
    }

    private void layoutComponents() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(totalLabel);
        panel.add(registeredLabel);
        panel.add(updatedLabel);
        panel.add(todayLabel);
        panel.add(refreshButton);

        add(panel, BorderLayout.CENTER);
    }

    private void registerListeners() {
        refreshButton.addActionListener(e -> loadStats());
    }

    private void loadStats() {
        try {
            int total = dashboardService.getTotalShipments();
            int reg = dashboardService.getRegisteredShipments();
            int upd = dashboardService.getUpdatedShipments();
            int today = dashboardService.getTodayShipments();

            totalLabel.setText("Total Shipments: " + total);
            registeredLabel.setText("Registered Shipments: " + reg);
            updatedLabel.setText("Updated Shipments: " + upd);
            todayLabel.setText("Shipments Today: " + today);

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
