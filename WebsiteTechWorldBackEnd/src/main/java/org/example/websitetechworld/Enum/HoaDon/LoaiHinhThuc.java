package org.example.websitetechworld.Enum.HoaDon;

public enum LoaiHinhThuc {
    TIEN_MAT("Tiền mặt"),
    NGAN_HANG("Ngân hàng");

    private final String displayName;

    LoaiHinhThuc(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
