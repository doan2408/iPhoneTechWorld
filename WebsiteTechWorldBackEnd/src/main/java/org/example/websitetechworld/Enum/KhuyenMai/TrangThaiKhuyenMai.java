package org.example.websitetechworld.Enum.KhuyenMai;

import lombok.Getter;

@Getter
public enum TrangThaiKhuyenMai {
    NOT_STARTED("Chưa bắt đầu"),
    ACTIVE("Đang hoạt động"),
    EXPIRED("Đã hết hạn");

    private final String displayName;
    TrangThaiKhuyenMai (String displayName) {
        this.displayName = displayName;
    }
}
