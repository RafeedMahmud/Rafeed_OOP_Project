package model;

import java.time.LocalDateTime;

public class Shipment implements Printable {


    private int id;
    private String trackingCode;

    // معلومات المرسل
    private String senderName;
    private String senderPhone;
    private String senderAddress;

    // معلومات المستلم
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    private double weight;
    private ShipmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;

    public Shipment(int id,
                    String trackingCode,
                    String senderName,
                    String senderPhone,
                    String senderAddress,
                    String receiverName,
                    String receiverPhone,
                    String receiverAddress,
                    double weight) {

        // مثال على Exception Handling بسيط
        if (trackingCode == null || trackingCode.isEmpty()) {
            throw new IllegalArgumentException("كود التتبع لا يمكن أن يكون فارغاً");
        }

        if (weight <= 0) {
            throw new IllegalArgumentException("الوزن يجب أن يكون أكبر من صفر");
        }

        this.id = id;
        this.trackingCode = trackingCode;
        this.senderName = senderName;
        this.senderPhone = senderPhone;
        this.senderAddress = senderAddress;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.weight = weight;

        this.status = ShipmentStatus.REGISTERED;  // أول حالة
        this.createdAt = LocalDateTime.now();
        this.deliveredAt = null;
    }

    // Getters فقط للاختصار (تقدر تضيف Setters فيما بعد لو تحتاج)
    public int getId() {
        return id;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public double getWeight() {
        return weight;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    // تغيير حالة الشحنة
    public void updateStatus(ShipmentStatus newStatus) {
        this.status = newStatus;

        if (newStatus == ShipmentStatus.DELIVERED) {
            this.deliveredAt = LocalDateTime.now();
        }
    }

        @Override
    public void printReceipt() {
        System.out.println("===== Shipment Receipt =====");
        System.out.println("Tracking Code : " + trackingCode);
        System.out.println("Sender        : " + senderName + " - " + senderPhone);
        System.out.println("From Address  : " + senderAddress);
        System.out.println("Receiver      : " + receiverName + " - " + receiverPhone);
        System.out.println("To Address    : " + receiverAddress);
        System.out.println("Weight        : " + weight + " kg");
        System.out.println("Status        : " + status);
        System.out.println("Created At    : " + createdAt);
        System.out.println("============================");
    }

}
