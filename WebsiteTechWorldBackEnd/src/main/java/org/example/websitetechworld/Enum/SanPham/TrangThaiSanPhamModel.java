package org.example.websitetechworld.Enum.SanPham;

public enum TrangThaiSanPhamModel {
    ACTIVE("Đang hoạt động"),
    DISCONTINUED("Ngừng sản xuất"),
    UPCOMING("Chờ ra mắt"),
    HIDDEN("Ẩn"),
    DELETED("Đã xóa");

    private final String displayNameModel;

    TrangThaiSanPhamModel(String displayNameModel) {
        this.displayNameModel = displayNameModel;
    }

    public String getDisplayNameModel() {
        return displayNameModel;
    }
}
