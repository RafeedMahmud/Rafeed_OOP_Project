package model;

import java.util.ArrayList;
import java.util.List;

public class InMemoryShipmentService implements ShipmentService {

    private final List<Shipment> shipments = new ArrayList<>();

    @Override
    public Shipment createShipment(
            int id,
            String trackingCode,
            String senderName,
            String senderPhone,
            String senderAddress,
            String receiverName,
            String receiverPhone,
            String receiverAddress,
            double weight
    ) {
        Shipment shipment = new Shipment(
                id,
                trackingCode,
                senderName,
                senderPhone,
                senderAddress,
                receiverName,
                receiverPhone,
                receiverAddress,
                weight
        );
        shipments.add(shipment);
        return shipment;
    }

    @Override
    public Shipment findByTrackingCode(String trackingCode) {
        for (Shipment s : shipments) {
            if (s.getTrackingCode().equalsIgnoreCase(trackingCode)) {
                return s;
            }
        }
        return null; // ممكن لاحقاً نرمي Exception بدال null
    }

    @Override
    public List<Shipment> getAllShipments() {
        return new ArrayList<>(shipments); // نرجّع نسخة عشان ما يتلعبش في الليست الأصلية
    }

    @Override
    public void updateStatus(String trackingCode, ShipmentStatus newStatus) {
        Shipment shipment = findByTrackingCode(trackingCode);
        if (shipment != null) {
            shipment.updateStatus(newStatus);
        } else {
            // هنا مثال بسيط على Exception Handling في الـ service
            throw new IllegalArgumentException("Shipment not found with tracking code: " + trackingCode);
        }
    }
}
