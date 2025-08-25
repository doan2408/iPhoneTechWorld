package org.example.websitetechworld.Enum.SanPham;

public enum TrangThaiSanPham {
    ACTIVE("Đang kinh doanh"),
    DISCONTINUED("Ngừng kinh doanh");
//    COMING_SOON("Sắp ra mắt"),
//    TEMPORARILY_UNAVAILABLE("Tạm ngừng bán"),
//    OUT_OF_STOCK("Hết hàng");

    private final String displayName;

    TrangThaiSanPham(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
