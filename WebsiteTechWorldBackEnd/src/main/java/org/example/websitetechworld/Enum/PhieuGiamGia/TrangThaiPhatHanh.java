package org.example.websitetechworld.Enum.PhieuGiamGia;

public enum TrangThaiPhatHanh {
    NOT_ISSUED("Chưa phát hành"),
    ISSUED("Đã phát hành");

    private final String displayName;

    TrangThaiPhatHanh(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
