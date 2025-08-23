package org.example.websitetechworld.Enum.HoaDon;

public enum TrangThaiThanhToan {
    PENDING("Chờ xử lý"),
    PAID("Đã thanh toán"),
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
