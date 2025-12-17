package ui;

import model.Shipment;
import service.LoggedUser;
import service.ServiceFactory;
import service.ShipmentService;

import javax.swing.*;
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
        setSize(900, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        registerListeners();
    }

    private void initComponents() {
        trackingCodeField = new JTextField(18);

        senderNameField = new JTextField(18);
        senderPhoneField = new JTextField(18);
        senderAddressField = new JTextField(18);

        receiverNameField = new JTextField(18);
        receiverPhoneField = new JTextField(18);
        receiverAddressField = new JTextField(18);

        weightField = new JTextField(18);

        infoLabel = new JLabel(" ");

        searchButton = new JButton("Search");
        updateInfoButton = new JButton("Update Info");
        backButton = new JButton("Back");
    }

    private void layoutComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));

        // TOP: Search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.add(new JLabel("Tracking Code:"));
        searchPanel.add(trackingCodeField);
        searchPanel.add(searchButton);

        main.add(searchPanel, BorderLayout.NORTH);

        // CENTER: Fields
        JPanel form = new JPanel(new GridLayout(4, 4, 10, 8));

        form.add(new JLabel("Sender Name:"));
        form.add(senderNameField);

        form.add(new JLabel("Sender Phone:"));
        form.add(senderPhoneField);

        form.add(new JLabel("Sender Address:"));
        form.add(senderAddressField);

        form.add(new JLabel("Receiver Name:"));
        form.add(receiverNameField);

        form.add(new JLabel("Receiver Phone:"));
        form.add(receiverPhoneField);

        form.add(new JLabel("Receiver Address:"));
        form.add(receiverAddressField);

        form.add(new JLabel("Weight:"));
        form.add(weightField);

        // filler cells
        form.add(new JLabel());
        form.add(new JLabel());
        form.add(new JLabel());
        form.add(new JLabel());

        main.add(form, BorderLayout.CENTER);

        // BOTTOM: Actions (زرّين فقط)
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        actions.add(updateInfoButton);
        actions.add(backButton);

        main.add(actions, BorderLayout.SOUTH);

        // INFO LABEL
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        infoPanel.add(infoLabel);
        main.add(infoPanel, BorderLayout.WEST);

        add(main);
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
