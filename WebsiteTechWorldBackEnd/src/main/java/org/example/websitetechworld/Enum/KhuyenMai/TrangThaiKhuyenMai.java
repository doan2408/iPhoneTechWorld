package org.example.websitetechworld.Enum.KhuyenMai;

import lombok.Getter;

@Getter
public enum TrangThaiKhuyenMai {
    ACTIVE("Đang hoạt động"),
    IN_ACTIVE("Hết hạn");

    private final String displayName;
    TrangThaiKhuyenMai (String displayName) {
        this.displayName = displayName;
    }
}
