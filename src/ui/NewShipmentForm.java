package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewShipmentForm extends JFrame {

    private JTextField senderNameField;
    private JTextField senderPhoneField;
    private JTextField senderAddressField;

    private JTextField receiverNameField;
    private JTextField receiverPhoneField;
    private JTextField receiverAddressField;

    private JTextField weightField;

    private JButton saveButton;

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

        String trackingCode = "TRK-" + System.currentTimeMillis();

        LocalDateTime now = LocalDateTime.now();
        String createdAtStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // ================================
        // SQLite connection (file-based DB)
        // ================================
        // this will create al_rahhala.db file in your project folder if not exist
        String url = "jdbc:sqlite:al_rahhala.db";

        // create table if not exists (SQLite syntax)
        String createTableSql =
                "CREATE TABLE IF NOT EXISTS shipments (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "tracking_code TEXT NOT NULL UNIQUE," +
                        "sender_name TEXT NOT NULL," +
                        "sender_phone TEXT," +
                        "sender_address TEXT," +
                        "receiver_name TEXT NOT NULL," +
                        "receiver_phone TEXT," +
                        "receiver_address TEXT," +
                        "weight REAL NOT NULL," +
                        "status TEXT NOT NULL," +
                        "created_at TEXT NOT NULL," +
                        "delivered_at TEXT," +
                        "created_by_user_id INTEGER" +
                ");";

        String insertSql =
                "INSERT INTO shipments (" +
                        "tracking_code, " +
                        "sender_name, sender_phone, sender_address, " +
                        "receiver_name, receiver_phone, receiver_address, " +
                        "weight, status, created_at, delivered_at, created_by_user_id" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, NULL)";

        try (Connection conn = DriverManager.getConnection(url)) {

            // create table if it does not exist
            try (Statement st = conn.createStatement()) {
                st.execute(createTableSql);
            }

            // insert shipment row
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setString(1, trackingCode);
                stmt.setString(2, senderName);
                stmt.setString(3, senderPhone);
                stmt.setString(4, senderAddress);
                stmt.setString(5, receiverName);
                stmt.setString(6, receiverPhone);
                stmt.setString(7, receiverAddress);
                stmt.setDouble(8, weight);
                stmt.setString(9, "REGISTERED");
                stmt.setString(10, createdAtStr);

                int rows = stmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Shipment saved successfully to SQLite.\nTracking Code: " + trackingCode,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "No rows affected.",
                            "Save Problem",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }

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
