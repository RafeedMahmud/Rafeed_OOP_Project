package ui;

import model.Shipment;
import service.ServiceFactory;
import service.SQLiteShipmentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class TrackingWindow extends JFrame {

    private JTextField trackingField;
    private JTextArea resultArea;
    private JButton searchButton;

    // UI-only addition (does not change business logic)
    private JButton clearButton;

    private final SQLiteShipmentService shipmentService =
            (SQLiteShipmentService) ServiceFactory.getShipmentService();

    public TrackingWindow() {
        setTitle("Tracking Search");
        setSize(620, 430);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        registerListeners();
    }

    private void initComponents() {
        trackingField = new JTextField(22);
        trackingField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        resultArea = new JTextArea(12, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchButton.setPreferredSize(new Dimension(120, 34));

        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        clearButton.setPreferredSize(new Dimension(120, 34));
    }

    private void layoutComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout(12, 12));
        rootPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        rootPanel.setBackground(new Color(245, 246, 250));

        JLabel titleLabel = new JLabel("Track Shipment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(5, 5, 10, 5));
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ===== Top Search Bar =====
        JPanel top = new JPanel(new GridBagLayout());
        top.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        JLabel trackingLabel = new JLabel("Tracking Code:");
        trackingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        gbc.gridx = 0;
        gbc.weightx = 0;
        top.add(trackingLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        top.add(trackingField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        top.add(searchButton, gbc);

        gbc.gridx = 3;
        top.add(clearButton, gbc);

        // ===== Result Area =====
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));

        cardPanel.add(top, BorderLayout.NORTH);
        cardPanel.add(scrollPane, BorderLayout.CENTER);

        rootPanel.add(cardPanel, BorderLayout.CENTER);

        setContentPane(rootPanel);
    }

    private void registerListeners() {
        searchButton.addActionListener(e -> doSearch());

        // UI-only: clear fields
        clearButton.addActionListener(e -> {
            trackingField.setText("");
            resultArea.setText("");
            trackingField.requestFocusInWindow();
        });
    }

    private void doSearch() {
        String trackingCode = trackingField.getText().trim();

        if (trackingCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter tracking code");
            return;
        }

        try {
            Shipment shipment = shipmentService.searchByTrackingCode(trackingCode);
            String status = shipmentService.getStatusFromDatabase(trackingCode);

            if (shipment == null) {
                resultArea.setText("Shipment not found.");
                return;
            }

            resultArea.setText(
                    "Tracking Code: " + shipment.getTrackingCode() + "\n" +
                    "Sender: " + shipment.getSenderName() + "\n" +
                    "Receiver: " + shipment.getReceiverName() + "\n" +
                    "Weight: " + shipment.getWeight() + "\n" +
                    "Status: " + status
            );

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error");
        }
    }
}
 