package ui;

import dao.ShipmentDAO;
import service.SQLiteShipmentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
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
        setTitle("Add New Shipment");
        setSize(600, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        layoutComponents();
        registerListeners();
    }

    private void initComponents() {
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        senderNameField = new JTextField(20);
        senderPhoneField = new JTextField(20);
        senderAddressField = new JTextField(20);

        receiverNameField = new JTextField(20);
        receiverPhoneField = new JTextField(20);
        receiverAddressField = new JTextField(20);

        weightField = new JTextField(10);

        senderNameField.setFont(fieldFont);
        senderPhoneField.setFont(fieldFont);
        senderAddressField.setFont(fieldFont);
        receiverNameField.setFont(fieldFont);
        receiverPhoneField.setFont(fieldFont);
        receiverAddressField.setFont(fieldFont);
        weightField.setFont(fieldFont);

        saveButton = new JButton("Save Shipment");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setPreferredSize(new Dimension(180, 40));
    }

    private void layoutComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout(15, 15));
        rootPanel.setBorder(new EmptyBorder(15, 20, 20, 20));
        rootPanel.setBackground(new Color(245, 246, 250));

        JLabel titleLabel = new JLabel("New Shipment Information", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(new EmptyBorder(5, 5, 15, 5));
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        formPanel.add(createSenderPanel());
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createReceiverPanel());
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createShipmentInfoPanel());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        buttonPanel.add(saveButton);

        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        rootPanel.add(cardPanel, BorderLayout.CENTER);
        setContentPane(rootPanel);
    }

    private JPanel createSenderPanel() {
        JPanel panel = createSectionPanel("Sender Information");

        addField(panel, "Sender Name:", senderNameField, 0);
        addField(panel, "Sender Phone:", senderPhoneField, 1);
        addField(panel, "Sender Address:", senderAddressField, 2);

        return panel;
    }

    private JPanel createReceiverPanel() {
        JPanel panel = createSectionPanel("Receiver Information");

        addField(panel, "Receiver Name:", receiverNameField, 0);
        addField(panel, "Receiver Phone:", receiverPhoneField, 1);
        addField(panel, "Receiver Address:", receiverAddressField, 2);

        return panel;
    }

    private JPanel createShipmentInfoPanel() {
        JPanel panel = createSectionPanel("Shipment Details");

        addField(panel, "Weight (kg):", weightField, 0);

        return panel;
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        title,
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14)
                )
        );
        return panel;
    }

    private void addField(JPanel panel, String labelText, JTextField field, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
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
                    "Shipment saved successfully.\nTracking Code: " + trackingCode,
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
