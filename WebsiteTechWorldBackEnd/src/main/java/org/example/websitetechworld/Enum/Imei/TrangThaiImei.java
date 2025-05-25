package org.example.websitetechworld.Enum.Imei;

public enum TrangThaiImei {
    AVAILABLE("Có sẵn"),
    RESERVED("Đã đặt trước"),
    SOLD("Đã bán"),
    RETURNED("Đã trả lại"),
    REFURBISHED("Tân trang"),
    BLACKLISTED("Bị chặn");

    private final String displayName;

    TrangThaiImei(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
