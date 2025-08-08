package org.example.websitetechworld.Enum.GiaoHang;

public enum TrangThaiGiaoHang {
    PENDING("Chờ xử lý"),
    CONFIRM("Xac nhan"),
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

    public static TrangThaiGiaoHang fromDisplayName(String displayName) {
        for (TrangThaiGiaoHang status : values()) {
            if (status.getDisplayName().equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Trạng thái không hợp lệ: " + displayName);
    }
}
