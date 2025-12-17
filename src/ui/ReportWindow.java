package ui;

import dao.ShipmentDAO;
import model.ShipmentStatus;
import service.ReportService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        setSize(820, 460);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        registerListeners();
    }

    private void initComponents() {
        table = new JTable();
        statusCombo = new JComboBox<>(ShipmentStatus.values());
        loadButton = new JButton("Load Report");

        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loadButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loadButton.setPreferredSize(new Dimension(140, 34));

        // Basic table styling (UI only)
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setReorderingAllowed(false);
    }

    private void layoutComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout(12, 12));
        rootPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        rootPanel.setBackground(new Color(245, 246, 250));

        JLabel titleLabel = new JLabel("Shipments Report (By Status)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(5, 5, 10, 5));
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel topBar = new JPanel(new GridBagLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(new EmptyBorder(12, 12, 12, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        gbc.gridx = 0;
        gbc.weightx = 0;
        topBar.add(statusLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        topBar.add(statusCombo, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        topBar.add(loadButton, gbc);

        JPanel centerCard = new JPanel(new BorderLayout());
        centerCard.setBackground(Color.WHITE);
        centerCard.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerCard.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout(12, 12));
        contentPanel.setBackground(new Color(245, 246, 250));
        contentPanel.add(topBar, BorderLayout.NORTH);
        contentPanel.add(centerCard, BorderLayout.CENTER);

        rootPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(rootPanel);
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

            // UI only: keep header styling after model change
            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 13));
            header.setReorderingAllowed(false);

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
 