package ui;

import model.Shipment;
import service.LoggedUser;
import service.ServiceFactory;
import service.ShipmentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ShipmentManagementWindow extends JFrame {

    private final LoggedUser loggedUser;
    private final ShipmentService shipmentService;

    private JTextField trackingCodeField;

    private JTextField senderNameField;
    private JTextField senderPhoneField;
    private JTextField senderAddressField;

    private JTextField receiverNameField;
    private JTextField receiverPhoneField;
    private JTextField receiverAddressField;

    private JTextField weightField;

    private JLabel infoLabel;

    private JButton searchButton;
    private JButton updateInfoButton;
    private JButton backButton;

    public ShipmentManagementWindow(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
        this.shipmentService = ServiceFactory.getShipmentService();

        setTitle("Al Rahhala - Shipment Management");
        setSize(900, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        registerListeners();
    }

    private void initComponents() {
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        trackingCodeField = new JTextField(18);
        trackingCodeField.setFont(fieldFont);

        senderNameField = new JTextField(18);
        senderPhoneField = new JTextField(18);
        senderAddressField = new JTextField(18);

        receiverNameField = new JTextField(18);
        receiverPhoneField = new JTextField(18);
        receiverAddressField = new JTextField(18);

        weightField = new JTextField(18);

        senderNameField.setFont(fieldFont);
        senderPhoneField.setFont(fieldFont);
        senderAddressField.setFont(fieldFont);
        receiverNameField.setFont(fieldFont);
        receiverPhoneField.setFont(fieldFont);
        receiverAddressField.setFont(fieldFont);
        weightField.setFont(fieldFont);

        infoLabel = new JLabel(" ");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        searchButton = new JButton("Search");
        updateInfoButton = new JButton("Update Info");
        backButton = new JButton("Back");

        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);
        Dimension btnSize = new Dimension(140, 36);

        searchButton.setFont(btnFont);
        updateInfoButton.setFont(btnFont);
        backButton.setFont(btnFont);

        searchButton.setPreferredSize(btnSize);
        updateInfoButton.setPreferredSize(btnSize);
        backButton.setPreferredSize(btnSize);
    }

    private void layoutComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout(12, 12));
        rootPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        rootPanel.setBackground(new Color(245, 246, 250));

        JLabel titleLabel = new JLabel("Shipment Information Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(new EmptyBorder(5, 5, 15, 5));
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        // ===== Search Bar =====
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        searchPanel.add(new JLabel("Tracking Code:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        searchPanel.add(trackingCodeField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        searchPanel.add(searchButton, gbc);

        // ===== Form Card =====
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        formCard.add(createSenderPanel());
        formCard.add(Box.createVerticalStrut(12));
        formCard.add(createReceiverPanel());
        formCard.add(Box.createVerticalStrut(12));
        formCard.add(createShipmentPanel());

        // ===== Actions =====
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionsPanel.setBackground(Color.WHITE);
        actionsPanel.add(updateInfoButton);
        actionsPanel.add(backButton);

        JPanel cardContainer = new JPanel(new BorderLayout(10, 10));
        cardContainer.setBackground(Color.WHITE);
        cardContainer.add(formCard, BorderLayout.CENTER);
        cardContainer.add(actionsPanel, BorderLayout.SOUTH);

        // ===== Info =====
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(245, 246, 250));
        infoPanel.add(infoLabel);

        JPanel centerPanel = new JPanel(new BorderLayout(12, 12));
        centerPanel.setBackground(new Color(245, 246, 250));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(cardContainer, BorderLayout.CENTER);
        centerPanel.add(infoPanel, BorderLayout.SOUTH);

        rootPanel.add(centerPanel, BorderLayout.CENTER);
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

    private JPanel createShipmentPanel() {
        JPanel panel = createSectionPanel("Shipment Details");
        addField(panel, "Weight:", weightField, 0);
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
        gbc.insets = new Insets(6, 8, 6, 8);
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
        searchButton.addActionListener(e -> doSearch());
        updateInfoButton.addActionListener(e -> doUpdateInfo());
        backButton.addActionListener(e -> dispose());
    }

    private void doSearch() {
        String trackingCode = trackingCodeField.getText().trim();
        if (trackingCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter tracking code", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Shipment s = shipmentService.findByTrackingCode(trackingCode);
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Shipment not found", "Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        senderNameField.setText(s.getSenderName());
        senderPhoneField.setText(s.getSenderPhone());
        senderAddressField.setText(s.getSenderAddress());

        receiverNameField.setText(s.getReceiverName());
        receiverPhoneField.setText(s.getReceiverPhone());
        receiverAddressField.setText(s.getReceiverAddress());

        weightField.setText(String.valueOf(s.getWeight()));

        infoLabel.setText("Loaded: " + s.getTrackingCode());
    }

    private void doUpdateInfo() {
        String trackingCode = trackingCodeField.getText().trim();
        if (trackingCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter tracking code first", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String senderName = senderNameField.getText().trim();
        String senderPhone = senderPhoneField.getText().trim();
        String senderAddress = senderAddressField.getText().trim();

        String receiverName = receiverNameField.getText().trim();
        String receiverPhone = receiverPhoneField.getText().trim();
        String receiverAddress = receiverAddressField.getText().trim();

        double weight;
        try {
            weight = Double.parseDouble(weightField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid weight", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean ok = shipmentService.updateShipmentInfo(
                trackingCode,
                senderName,
                senderPhone,
                senderAddress,
                receiverName,
                receiverPhone,
                receiverAddress,
                weight
        );

        if (ok) {
            JOptionPane.showMessageDialog(this, "Shipment updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No changes applied", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
 