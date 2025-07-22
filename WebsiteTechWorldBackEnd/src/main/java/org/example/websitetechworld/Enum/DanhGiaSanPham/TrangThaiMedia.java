package org.example.websitetechworld.Enum.DanhGiaSanPham;

public enum TrangThaiMedia {
    PENDING_APPROVAL("Chờ duyệt"),
    APPROVED("Đã duyệt"),
    REFUSE("Từ chối"),
    HIDE("Ẩn");

    private final String displayName;

    TrangThaiMedia(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
