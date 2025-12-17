package ui;

import model.ShipmentStatus;
import service.ServiceFactory;
import service.SQLiteShipmentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class UpdateStatusWindow extends JFrame {

    private JTextField trackingField;
    private JComboBox<ShipmentStatus> statusCombo;
    private JButton updateButton;

    private final SQLiteShipmentService shipmentService =
            (SQLiteShipmentService) ServiceFactory.getShipmentService();

    public UpdateStatusWindow() {
        setTitle("Update Shipment Status");
        setSize(560, 330);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        registerListeners();
    }

    private void initComponents() {
        trackingField = new JTextField(20);
        trackingField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        statusCombo = new JComboBox<>(ShipmentStatus.values());
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        updateButton = new JButton("Update Status");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        updateButton.setPreferredSize(new Dimension(160, 38));
    }

    private void layoutComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout(12, 12));
        rootPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        rootPanel.setBackground(new Color(245, 246, 250));

        JLabel titleLabel = new JLabel("Update Shipment Status", SwingConstants.CENTER);
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
        gbc.gridy = 0;

        JLabel trackingLabel = new JLabel("Tracking Code:");
        trackingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        formPanel.add(trackingLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(trackingField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0.3;

        JLabel statusLabel = new JLabel("New Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(statusLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(statusCombo, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonPanel.add(updateButton);

        cardPanel.add(formPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        rootPanel.add(cardPanel, BorderLayout.CENTER);

        setContentPane(rootPanel);
    }

    private void registerListeners() {
        updateButton.addActionListener(e -> doUpdate());
    }

    private void doUpdate() {
        try {
            boolean updated = shipmentService.updateStatusSafe(
                    trackingField.getText().trim(),
                    (ShipmentStatus) statusCombo.getSelectedItem()
            );

            if (!updated) {
                JOptionPane.showMessageDialog(this, "Tracking code not found");
                return;
            }

            JOptionPane.showMessageDialog(this, "Status updated successfully");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error");
        }
    }
}
 