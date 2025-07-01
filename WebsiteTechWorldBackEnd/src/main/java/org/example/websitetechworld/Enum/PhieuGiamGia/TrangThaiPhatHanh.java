package org.example.websitetechworld.Enum.PhieuGiamGia;

public enum TrangThaiPGG {
    NOT_STARTED("Chưa bắt đầu"),
    ACTIVE("Đang hoạt động"),
    EXPIRED("Đã hết hạn");

    private final String displayName;

    TrangThaiPGG(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
