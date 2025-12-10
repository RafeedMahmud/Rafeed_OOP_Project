import model.*;

public class Main {
    public static void main(String[] args) {

        ShipmentService service = new InMemoryShipmentService();

        Shipment shipment = service.createShipment(
                1,
                "TRK-2001",
                "Ali Sender",
                "0911111111",
                "Benghazi",
                "Omar Receiver",
                "0922222222",
                "Al Bayda",
                5.0
        );

        shipment.printReceipt();

        service.updateStatus("TRK-2001", ShipmentStatus.OUT_FOR_DELIVERY);
        System.out.println("New Status: " + shipment.getStatus());
    }
}
