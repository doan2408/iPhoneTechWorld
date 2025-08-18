package org.example.websitetechworld.Enum.HoaDon;

public enum TrangThaiThanhToan {
    PENDING("Chờ thanh toán"),
    CONFIRMED("Đã xác nhận"),
    PAID("Đã thanh toán"),
    CANCELLED("Đã hủy"),
    REFUNDED("Đã hoàn tiền"),
    COMPLETED("Hoàn tất");

    private final String displayName;

    TrangThaiThanhToan(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
