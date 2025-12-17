package ui;

import dao.ShipmentDAO;
import model.ShipmentStatus;
import service.ReportService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class ReportWindow extends JFrame {

    private JTable table;
    private JComboBox<ShipmentStatus> statusCombo;
    private JButton loadButton;

    private final ReportService reportService =
            new ReportService(new ShipmentDAO());

    public ReportWindow() {
        setTitle("Reports - Shipments By Status");
        setSize(650, 360);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        registerListeners();
    }

    private void initComponents() {
        table = new JTable();
        statusCombo = new JComboBox<>(ShipmentStatus.values());
        loadButton = new JButton("Load Report");
    }

    private void layoutComponents() {
        JPanel top = new JPanel();
        top.add(new JLabel("Status:"));
        top.add(statusCombo);
        top.add(loadButton);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void registerListeners() {
        loadButton.addActionListener(e -> loadReport());
    }

    private void loadReport() {
        ShipmentStatus status = (ShipmentStatus) statusCombo.getSelectedItem();

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Tracking Code", "Sender", "Receiver", "Status"}, 0
        );

        try (ResultSet rs = reportService.getShipmentsByStatus(status.name())) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("tracking_code"),
                        rs.getString("sender_name"),
                        rs.getString("receiver_name"),
                        rs.getString("status")
                });
            }

            table.setModel(model);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error loading report",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
