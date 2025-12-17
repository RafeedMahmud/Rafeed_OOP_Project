package ui;

import dao.ShipmentDAO;
import model.ShipmentStatus;
import service.SQLiteShipmentService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class UpdateStatusWindow extends JFrame {

    private JTextField trackingField;
    private JComboBox<ShipmentStatus> statusCombo;
    private JButton updateButton;

    private final SQLiteShipmentService shipmentService =
            new SQLiteShipmentService(new ShipmentDAO());

    public UpdateStatusWindow() {
        setTitle("Update Shipment Status");
        setSize(400, 200);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        registerListeners();
    }

    private void initComponents() {
        trackingField = new JTextField(15);
        statusCombo = new JComboBox<>(ShipmentStatus.values());
        updateButton = new JButton("Update Status");
    }

    private void layoutComponents() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        panel.add(new JLabel("Tracking Code:"));
        panel.add(trackingField);

        panel.add(new JLabel("New Status:"));
        panel.add(statusCombo);

        panel.add(new JLabel());
        panel.add(updateButton);

        add(panel);
    }

    private void registerListeners() {
        updateButton.addActionListener(e -> doUpdate());
    }

    private void doUpdate() {
        String trackingCode = trackingField.getText().trim();
        ShipmentStatus status = (ShipmentStatus) statusCombo.getSelectedItem();

        if (trackingCode.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter tracking code",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            boolean updated = shipmentService.updateStatusSafe(trackingCode, status);

            if (!updated) {
                JOptionPane.showMessageDialog(
                        this,
                        "Tracking Code not found (no update happened).",
                        "Not Found",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Status updated successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

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
