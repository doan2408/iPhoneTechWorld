package org.example.websitetechworld.Enum.KhuyenMai;

public enum DoiTuongApDung {
    ALL("Tất cả"),
    NEW_CUSTOMER("Khách hàng mới"),
    OLD_CUSTOMER("Khách hàng cũ");

    private final String displayName;

    DoiTuongApDung(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
