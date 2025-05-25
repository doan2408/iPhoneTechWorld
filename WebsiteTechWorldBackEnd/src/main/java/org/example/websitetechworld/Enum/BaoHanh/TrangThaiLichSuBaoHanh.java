package org.example.websitetechworld.Enum.BaoHanh;

public enum TrangThaiLichSuBaoHanh {
    IN_REPAIR("Đang sửa chữa"),
    REPAIRED("Đã sửa chữa"),
    WARRANTY_VOID("Hết bảo hành");

    private final String displayName;

    TrangThaiLichSuBaoHanh(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
