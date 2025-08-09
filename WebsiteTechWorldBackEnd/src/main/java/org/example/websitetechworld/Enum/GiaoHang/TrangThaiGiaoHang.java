package org.example.websitetechworld.Enum.GiaoHang;

public enum TrangThaiGiaoHang {
    PENDING("Chờ xử lý"),
    CONFIRM("Đã xác nhận"),
    PACKED("Đã đóng gói"),
    READYFORPICKUP("Sẵn sàng giao"),
    SHIPPING("Đang giao"),
    DELIVERED("Đã giao"),
    FAILED("Giao thất bại"),
    RETURNED("Đã trả lại"),
    CANCELLED("Đã hủy");

    private final String displayName;

    TrangThaiGiaoHang(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TrangThaiGiaoHang fromDisplayName(String displayName) {
        for (TrangThaiGiaoHang status : values()) {
            if (status.getDisplayName().equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Trạng thái không hợp lệ: " + displayName);
    }
}
