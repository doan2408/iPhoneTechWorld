package org.example.websitetechworld.Enum.BaoHanh;

public enum TrangThaiBaoHanh {
    UNDER_WARRANTY("Còn bảo hành"),
    WARRANTY_EXPIRED("Hết bảo hành"),
    WARRANTY_VOID("Không bảo hành");

    private final String displayName;

    TrangThaiBaoHanh(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
