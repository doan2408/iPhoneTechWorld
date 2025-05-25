package org.example.websitetechworld.Enum.KhachHang;

public enum HangKhachHang {
    MEMBER("Thành viên"),
    SILVER("Bạc"),
    GOLD("Vàng"),
    DIAMOND("Kim cương");

    private final String displayName;

    HangKhachHang(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
