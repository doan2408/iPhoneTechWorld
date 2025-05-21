package org.example.websitetechworld.Enum.NhanVien;

public enum NhanVienTrangThai {
    ENABLE("Hoạt động"),
    DISABLE("Ngừng hoạt động");

    private final String displayName;

    NhanVienTrangThai(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
