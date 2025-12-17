package dao;

import model.Shipment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ShipmentDAO {

    private static final String CREATE_TABLE_SQL =
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
                    "created_at TEXT NOT NULL" +
            ");";

    private static final String INSERT_SQL =
            "INSERT INTO shipments (" +
                    "tracking_code, sender_name, sender_phone, sender_address, " +
                    "receiver_name, receiver_phone, receiver_address, " +
                    "weight, status, created_at" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public ShipmentDAO() {
        try {
            ensureTableExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ensureTableExists() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL);
        }
    }

    // ===============================
    // INSERT
    // ===============================
    public int insertShipment(
            String trackingCode,
            String senderName,
            String senderPhone,
            String senderAddress,
            String receiverName,
            String receiverPhone,
            String receiverAddress,
            double weight,
            String status,
            String createdAt
    ) throws SQLException {

        try (PreparedStatement ps =
                     DBConnection.getConnection().prepareStatement(INSERT_SQL)) {

            ps.setString(1, trackingCode);
            ps.setString(2, senderName);
            ps.setString(3, senderPhone);
            ps.setString(4, senderAddress);
            ps.setString(5, receiverName);
            ps.setString(6, receiverPhone);
            ps.setString(7, receiverAddress);
            ps.setDouble(8, weight);
            ps.setString(9, status);
            ps.setString(10, createdAt);

            return ps.executeUpdate();
        }
    }

    // ===============================
    // FIND (Shipment details)
    // ===============================
    public Shipment findShipmentByTrackingCode(String trackingCode) throws SQLException {

        String sql = "SELECT * FROM shipments WHERE tracking_code = ?";

        try (PreparedStatement ps =
                     DBConnection.getConnection().prepareStatement(sql)) {

            ps.setString(1, trackingCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Shipment(
                            rs.getInt("id"),
                            rs.getString("tracking_code"),
                            rs.getString("sender_name"),
                            rs.getString("sender_phone"),
                            rs.getString("sender_address"),
                            rs.getString("receiver_name"),
                            rs.getString("receiver_phone"),
                            rs.getString("receiver_address"),
                            rs.getDouble("weight")
                    );
                }
            }
        }

        return null;
    }

    // ===============================
    // FIND status only
    // ===============================
    public String findStatusByTrackingCode(String trackingCode) throws SQLException {
        String sql = "SELECT status FROM shipments WHERE tracking_code = ?";

        try (PreparedStatement ps =
                     DBConnection.getConnection().prepareStatement(sql)) {

            ps.setString(1, trackingCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status");
                }
            }
        }

        return null;
    }

    // ===============================
    // UPDATE STATUS
    // ===============================
    public int updateStatus(String trackingCode, String newStatus) throws SQLException {

        String sql = "UPDATE shipments SET status = ? WHERE tracking_code = ?";

        try (PreparedStatement ps =
                     DBConnection.getConnection().prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setString(2, trackingCode);

            return ps.executeUpdate();
        }
    }

    // ===============================
    // DASHBOARD COUNTS
    // ===============================
    public int countAllShipments() throws SQLException {
        String sql = "SELECT COUNT(*) FROM shipments";

        try (PreparedStatement ps =
                     DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int countRegisteredShipments() throws SQLException {
        String sql = "SELECT COUNT(*) FROM shipments WHERE status = 'REGISTERED'";

        try (PreparedStatement ps =
                     DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int countUpdatedShipments() throws SQLException {
        String sql = "SELECT COUNT(*) FROM shipments WHERE status <> 'REGISTERED'";

        try (PreparedStatement ps =
                     DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int countTodayShipments(String todayPrefix) throws SQLException {
        // todayPrefix مثال: "2025-12-17"
        String sql = "SELECT COUNT(*) FROM shipments WHERE created_at LIKE ?";

        try (PreparedStatement ps =
                     DBConnection.getConnection().prepareStatement(sql)) {

            ps.setString(1, todayPrefix + "%");

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
    public ResultSet getShipmentsByStatus(String status) throws SQLException {
    String sql = "SELECT tracking_code, sender_name, receiver_name, status FROM shipments WHERE status = ?";

    PreparedStatement ps =
            DBConnection.getConnection().prepareStatement(sql);
    ps.setString(1, status);

    return ps.executeQuery();
}

public ResultSet getAllShipmentsForReport() throws SQLException {
    String sql = "SELECT tracking_code, sender_name, receiver_name, status FROM shipments";

    PreparedStatement ps =
            DBConnection.getConnection().prepareStatement(sql);

    return ps.executeQuery();
}

}
