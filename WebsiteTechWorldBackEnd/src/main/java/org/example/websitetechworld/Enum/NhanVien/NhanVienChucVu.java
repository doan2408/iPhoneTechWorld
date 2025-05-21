package org.example.websitetechworld.Enum.NhanVien;

public enum NhanVienChucVu {
    STAFF("Nhân viên"),
    ADMIN("Quản trị viên");

    private final String displayName;

    NhanVienChucVu(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
