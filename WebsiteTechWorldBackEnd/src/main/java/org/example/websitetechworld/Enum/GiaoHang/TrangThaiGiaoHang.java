package org.example.websitetechworld.Enum.GiaoHang;

public enum TrangThaiGiaoHang {
    PENDING("Chờ xử lý"),
    PACKED("Đã đóng gói"),
    SHIPPING("Đang giao"),
    DELIVERED("Đã giao"),
    FAILED("Giao thất bại"),
    RETURNED("Đã trả lại");

    private final String displayName;

    TrangThaiGiaoHang(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
