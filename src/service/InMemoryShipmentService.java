package service;

import java.util.ArrayList;
import java.util.List;

import model.Shipment;
import model.ShipmentStatus;

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
        return null;
    }

    @Override
    public List<Shipment> getAllShipments() {
        return new ArrayList<>(shipments);
    }

    @Override
    public void updateStatus(String trackingCode, ShipmentStatus newStatus) {
        Shipment shipment = findByTrackingCode(trackingCode);
        if (shipment != null) {
            shipment.updateStatus(newStatus);
        } else {
            throw new IllegalArgumentException("Shipment not found with tracking code: " + trackingCode);
        }
    }

    // NEW: required by ShipmentService after adding updateShipmentInfo
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
        Shipment shipment = findByTrackingCode(trackingCode);
        if (shipment == null) {
            throw new IllegalArgumentException("Shipment not found with tracking code: " + trackingCode);
        }

        // ملاحظة: Shipment model عندك حالياً ما فيه Setters لتغيير البيانات
        // لذلك نبدل الشحنة بكائن جديد بنفس trackingCode و id
        Shipment updated = new Shipment(
                shipment.getId(),
                shipment.getTrackingCode(),
                senderName,
                senderPhone,
                senderAddress,
                receiverName,
                receiverPhone,
                receiverAddress,
                weight
        );

        // نحافظ على الحالة الحالية إن كانت مختلفة عن REGISTERED
        if (shipment.getStatus() != null && shipment.getStatus() != ShipmentStatus.REGISTERED) {
            updated.updateStatus(shipment.getStatus());
        }

        // استبدال داخل الليست
        int index = shipments.indexOf(shipment);
        if (index >= 0) {
            shipments.set(index, updated);
            return true;
        }

        return false;
    }
}
