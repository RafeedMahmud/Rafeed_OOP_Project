package ui;

import service.LoggedUser;

import javax.swing.*;
import java.awt.*;

public class ShipmentMenuWindow extends JFrame {

    private final LoggedUser loggedUser;

    public ShipmentMenuWindow(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;

        setTitle("Al Rahhala - Shipments");
        setSize(750, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton addShipmentButton = new JButton("Add New Shipment");
        addShipmentButton.addActionListener(e -> new NewShipmentForm().setVisible(true));

        JButton trackingButton = new JButton("Track Shipment");
        trackingButton.addActionListener(e -> new TrackingWindow().setVisible(true));

        JButton updateStatusButton = new JButton("Update Status");
        updateStatusButton.addActionListener(e -> new UpdateStatusWindow().setVisible(true));

        // NEW: زر يفتح صفحة تعديل معلومات الشحنة
        JButton editShipmentInfoButton = new JButton("Edit Shipment Info");
        editShipmentInfoButton.addActionListener(e -> new ShipmentManagementWindow(loggedUser).setVisible(true));

        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
        add(addShipmentButton);
        add(trackingButton);
        add(updateStatusButton);
        add(editShipmentInfoButton);

        // زر مختلف حسب الدور
        if (loggedUser.isAdmin()) {
            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> dispose());
            add(backButton);
        } else {
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
            add(logoutButton);
        }
    }
}
