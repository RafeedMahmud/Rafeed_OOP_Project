package model;

import java.util.List;

public interface ShipmentService {

    Shipment createShipment(
            int id,
            String trackingCode,
            String senderName,
            String senderPhone,
            String senderAddress,
            String receiverName,
            String receiverPhone,
            String receiverAddress,
            double weight
    );

    Shipment findByTrackingCode(String trackingCode);

    List<Shipment> getAllShipments();

    void updateStatus(String trackingCode, ShipmentStatus newStatus);
}
