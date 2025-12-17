package ui;

import dao.ShipmentDAO;
import service.SQLiteShipmentService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class NewShipmentForm extends JFrame {

    private JTextField senderNameField;
    private JTextField senderPhoneField;
    private JTextField senderAddressField;

    private JTextField receiverNameField;
    private JTextField receiverPhoneField;
    private JTextField receiverAddressField;

    private JTextField weightField;

    private JButton saveButton;

    // Service Layer (بدل SQL داخل الـ UI)
    private final SQLiteShipmentService shipmentService =
            new SQLiteShipmentService(new ShipmentDAO());

    public NewShipmentForm() {
        setTitle("Add New Shipment (SQLite)");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        layoutComponents();
        registerListeners();
    }

    private void initComponents() {
        senderNameField = new JTextField(20);
        senderPhoneField = new JTextField(20);
        senderAddressField = new JTextField(20);

        receiverNameField = new JTextField(20);
        receiverPhoneField = new JTextField(20);
        receiverAddressField = new JTextField(20);

        weightField = new JTextField(10);

        saveButton = new JButton("Save to SQLite Database");
    }

    private void layoutComponents() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));

        panel.add(new JLabel("Sender Name:"));
        panel.add(senderNameField);

        panel.add(new JLabel("Sender Phone:"));
        panel.add(senderPhoneField);

        panel.add(new JLabel("Sender Address:"));
        panel.add(senderAddressField);

        panel.add(new JLabel("Receiver Name:"));
        panel.add(receiverNameField);

        panel.add(new JLabel("Receiver Phone:"));
        panel.add(receiverPhoneField);

        panel.add(new JLabel("Receiver Address:"));
        panel.add(receiverAddressField);

        panel.add(new JLabel("Weight (kg):"));
        panel.add(weightField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void registerListeners() {
        saveButton.addActionListener(e -> saveShipmentToDatabase());
    }

    private void saveShipmentToDatabase() {
        String senderName = senderNameField.getText().trim();
        String senderPhone = senderPhoneField.getText().trim();
        String senderAddress = senderAddressField.getText().trim();

        String receiverName = receiverNameField.getText().trim();
        String receiverPhone = receiverPhoneField.getText().trim();
        String receiverAddress = receiverAddressField.getText().trim();

        String weightText = weightField.getText().trim();

        // basic input validation
        if (senderName.isEmpty() || receiverName.isEmpty() || weightText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill Sender Name, Receiver Name, and Weight.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        double weight;
        try {
            weight = Double.parseDouble(weightText);
            if (weight <= 0) {
                throw new NumberFormatException("Weight must be positive");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid weight (number > 0).",
                    "Weight Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // بدل SQL هنا: نمرر البيانات للـ Service
        try {
            String trackingCode = shipmentService.saveNewShipment(
                    senderName,
                    senderPhone,
                    senderAddress,
                    receiverName,
                    receiverPhone,
                    receiverAddress,
                    weight
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Shipment saved successfully to SQLite.\nTracking Code: " + trackingCode,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            clearForm();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "SQLite error:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearForm() {
        senderNameField.setText("");
        senderPhoneField.setText("");
        senderAddressField.setText("");

        receiverNameField.setText("");
        receiverPhoneField.setText("");
        receiverAddressField.setText("");

        weightField.setText("");
    }
}
