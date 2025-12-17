package ui;

import dao.ShipmentDAO;
import model.Shipment;
import service.SQLiteShipmentService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class TrackingWindow extends JFrame {

    private JTextField trackingField;
    private JTextArea resultArea;
    private JButton searchButton;

    private final SQLiteShipmentService shipmentService =
            new SQLiteShipmentService(new ShipmentDAO());

    public TrackingWindow() {
        setTitle("Tracking Search");
        setSize(420, 320);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        registerListeners();
    }

    private void initComponents() {
        trackingField = new JTextField(20);
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        searchButton = new JButton("Search");
    }

    private void layoutComponents() {
        JPanel top = new JPanel();
        top.add(new JLabel("Tracking Code:"));
        top.add(trackingField);
        top.add(searchButton);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
    }

    private void registerListeners() {
        searchButton.addActionListener(e -> doSearch());
    }

    private void doSearch() {
        String trackingCode = trackingField.getText().trim();

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
            Shipment shipment = shipmentService.searchByTrackingCode(trackingCode);

            if (shipment == null) {
                resultArea.setText("Shipment not found.");
                return;
            }

            String statusFromDb = shipmentService.getStatusFromDatabase(trackingCode);
            if (statusFromDb == null) statusFromDb = "UNKNOWN";

            resultArea.setText(
                    "Tracking Code: " + shipment.getTrackingCode() + "\n" +
                    "Sender: " + shipment.getSenderName() + "\n" +
                    "Receiver: " + shipment.getReceiverName() + "\n" +
                    "Weight: " + shipment.getWeight() + "\n" +
                    "Status (DB): " + statusFromDb
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
