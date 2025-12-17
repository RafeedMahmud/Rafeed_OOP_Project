package service;

import dao.ShipmentDAO;
import model.Shipment;
import model.ShipmentStatus;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SQLiteShipmentService implements ShipmentService {

    private final ShipmentDAO shipmentDAO;

    public SQLiteShipmentService(ShipmentDAO shipmentDAO) {
        this.shipmentDAO = shipmentDAO;
    }

    public String saveNewShipment(
            String senderName,
            String senderPhone,
            String senderAddress,
            String receiverName,
            String receiverPhone,
            String receiverAddress,
            double weight
    ) throws SQLException {

        String trackingCode = "TRK-" + System.currentTimeMillis();
        String createdAtStr = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        int rows = shipmentDAO.insertShipment(
                trackingCode,
                senderName, senderPhone, senderAddress,
                receiverName, receiverPhone, receiverAddress,
                weight,
                ShipmentStatus.REGISTERED.name(),
                createdAtStr
        );

        if (rows <= 0) {
            throw new SQLException("No rows affected.");
        }

        return trackingCode;
    }

    public Shipment searchByTrackingCode(String trackingCode) throws SQLException {
        return shipmentDAO.findShipmentByTrackingCode(trackingCode);
    }

    public String getStatusFromDatabase(String trackingCode) throws SQLException {
        return shipmentDAO.findStatusByTrackingCode(trackingCode);
    }

    public boolean updateStatusSafe(String trackingCode, ShipmentStatus newStatus) throws SQLException {
        int rows = shipmentDAO.updateStatus(trackingCode, newStatus.name());
        return rows > 0;
    }

    // NEW: Update shipment info (safe)
    public boolean updateShipmentInfoSafe(
            String trackingCode,
            String senderName,
            String senderPhone,
            String senderAddress,
            String receiverName,
            String receiverPhone,
            String receiverAddress,
            double weight
    ) throws SQLException {
        int rows = shipmentDAO.updateShipmentInfo(
                trackingCode,
                senderName,
                senderPhone,
                senderAddress,
                receiverName,
                receiverPhone,
                receiverAddress,
                weight
        );
        return rows > 0;
    }

    // ====== interface ======
    @Override
    public Shipment createShipment(int id, String trackingCode, String senderName, String senderPhone, String senderAddress,
                                   String receiverName, String receiverPhone, String receiverAddress, double weight) {
        return null;
    }

    @Override
    public Shipment findByTrackingCode(String trackingCode) {
        try {
            return searchByTrackingCode(trackingCode);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Shipment> getAllShipments() {
        return new ArrayList<>();
    }

    @Override
    public void updateStatus(String trackingCode, ShipmentStatus newStatus) {
        try {
            updateStatusSafe(trackingCode, newStatus);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateShipmentInfo(
            String trackingCode,
            String senderName,
            String senderPhone,
            String senderAddress,
            String receiverName,
            String receiverPhone,
            String receiverAddress,
            double weight
    ) {
        try {
            return updateShipmentInfoSafe(
                    trackingCode,
                    senderName,
                    senderPhone,
                    senderAddress,
                    receiverName,
                    receiverPhone,
                    receiverAddress,
                    weight
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
