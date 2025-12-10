package ui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Al Rahhala - Main Window");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton addShipmentButton = new JButton("Add New Shipment");

        addShipmentButton.addActionListener(e -> {
            NewShipmentForm form = new NewShipmentForm();
            form.setVisible(true);
        });

        setLayout(new FlowLayout());
        add(addShipmentButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
