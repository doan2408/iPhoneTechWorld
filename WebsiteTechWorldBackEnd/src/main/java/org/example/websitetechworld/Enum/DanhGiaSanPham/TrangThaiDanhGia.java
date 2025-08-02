package org.example.websitetechworld.Enum.DanhGiaSanPham;

public enum TrangThaiDanhGia {
    PENDING_APPROVAL("Chờ duyệt"),
    APPROVED("Đã duyệt"),
    REFUSE("Từ chối"),
    HIDE("Ẩn");

    private final String displayName;

    TrangThaiDanhGia(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
