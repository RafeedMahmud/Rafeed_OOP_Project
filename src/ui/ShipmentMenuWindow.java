package ui;

import service.LoggedUser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ShipmentMenuWindow extends JFrame {

    private final LoggedUser loggedUser;

    public ShipmentMenuWindow(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;

        setTitle("Al Rahhala - Shipments");
        setSize(820, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== Root =====
        JPanel rootPanel = new JPanel(new BorderLayout(12, 12));
        rootPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        rootPanel.setBackground(new Color(245, 246, 250));

        JLabel titleLabel = new JLabel("Shipments Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(new EmptyBorder(5, 5, 15, 5));
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        // ===== Card =====
        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ===== Buttons Grid =====
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(Color.WHITE);

        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);
        Dimension btnSize = new Dimension(220, 42);

        JButton addShipmentButton = new JButton("Add New Shipment");
        addShipmentButton.setFont(btnFont);
        addShipmentButton.setPreferredSize(btnSize);
        addShipmentButton.addActionListener(e -> new NewShipmentForm().setVisible(true));

        JButton trackingButton = new JButton("Track Shipment");
        trackingButton.setFont(btnFont);
        trackingButton.setPreferredSize(btnSize);
        trackingButton.addActionListener(e -> new TrackingWindow().setVisible(true));

        JButton updateStatusButton = new JButton("Update Status");
        updateStatusButton.setFont(btnFont);
        updateStatusButton.setPreferredSize(btnSize);
        updateStatusButton.addActionListener(e -> new UpdateStatusWindow().setVisible(true));

        JButton editShipmentInfoButton = new JButton("Edit Shipment Info");
        editShipmentInfoButton.setFont(btnFont);
        editShipmentInfoButton.setPreferredSize(btnSize);
        editShipmentInfoButton.addActionListener(e -> new ShipmentManagementWindow(loggedUser).setVisible(true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gridPanel.add(addShipmentButton, gbc);

        gbc.gridx = 1;
        gridPanel.add(trackingButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gridPanel.add(updateStatusButton, gbc);

        gbc.gridx = 1;
        gridPanel.add(editShipmentInfoButton, gbc);

        cardPanel.add(gridPanel, BorderLayout.CENTER);

        // ===== Footer (Back / Logout) =====
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        footerPanel.setBackground(Color.WHITE);

        if (loggedUser.isAdmin()) {
            JButton backButton = new JButton("Back");
            backButton.setFont(btnFont);
            backButton.setPreferredSize(new Dimension(140, 38));
            backButton.addActionListener(e -> dispose());
            footerPanel.add(backButton);
        } else {
            JButton logoutButton = new JButton("Logout");
            logoutButton.setFont(btnFont);
            logoutButton.setPreferredSize(new Dimension(140, 38));
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
            footerPanel.add(logoutButton);
        }

        cardPanel.add(footerPanel, BorderLayout.SOUTH);

        rootPanel.add(cardPanel, BorderLayout.CENTER);
        setContentPane(rootPanel);
    }
}
 