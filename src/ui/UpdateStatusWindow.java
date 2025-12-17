package ui;

import model.ShipmentStatus;
import service.ServiceFactory;
import service.SQLiteShipmentService;

import javax.swing.*;
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
