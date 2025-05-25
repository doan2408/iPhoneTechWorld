package org.example.websitetechworld.Enum.KhachHang;

public enum TrangThaiKhachHang {
    ACTIVE("Hoạt động"),
    INACTIVE("Không hoạt động");

    private final String displayName;

    TrangThaiKhachHang(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
