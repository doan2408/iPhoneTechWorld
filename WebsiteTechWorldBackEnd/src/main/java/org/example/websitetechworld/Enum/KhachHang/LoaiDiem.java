package org.example.websitetechworld.Enum.KhachHang;

public enum LoaiDiem {
    CONG("+++"),
    TRU("---");

    private final String displayName;
    private LoaiDiem(String displayName) {
        this.displayName = displayName;
    }

    private String getDisplayName() {
        return displayName;
    }
}
