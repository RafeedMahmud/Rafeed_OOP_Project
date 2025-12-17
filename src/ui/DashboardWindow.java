package ui;

import service.DashboardService;
import service.ServiceFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setSize(760, 420);
        setLocationRelativeTo(null);

        initComponents();
        loadStats();
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout(12, 12));
        rootPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        rootPanel.setBackground(new Color(245, 246, 250));

        JLabel titleLabel = new JLabel("Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(new EmptyBorder(5, 5, 10, 5));
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        // ===== Cards Grid =====
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(new Color(245, 246, 250));

        totalLabel = createStatCardLabel();
        registeredLabel = createStatCardLabel();
        updatedLabel = createStatCardLabel();
        todayLabel = createStatCardLabel();

        JPanel card1 = createCard("Total Shipments", totalLabel);
        JPanel card2 = createCard("Registered Shipments", registeredLabel);
        JPanel card3 = createCard("Updated Shipments", updatedLabel);
        JPanel card4 = createCard("Shipments Today", todayLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        grid.add(card1, gbc);

        gbc.gridx = 1;
        grid.add(card2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        grid.add(card3, gbc);

        gbc.gridx = 1;
        grid.add(card4, gbc);

        rootPanel.add(grid, BorderLayout.CENTER);
        setContentPane(rootPanel);
    }

    private JLabel createStatCardLabel() {
        JLabel value = new JLabel("—", SwingConstants.LEFT);
        value.setFont(new Font("Segoe UI", Font.BOLD, 26));
        return value;
    }

    private JPanel createCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void loadStats() {
        try {
            totalLabel.setText(String.valueOf(dashboardService.getTotalShipments()));
            registeredLabel.setText(String.valueOf(dashboardService.getRegisteredShipments()));
            updatedLabel.setText(String.valueOf(dashboardService.getUpdatedShipments()));
            todayLabel.setText(String.valueOf(dashboardService.getTodayShipments()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
