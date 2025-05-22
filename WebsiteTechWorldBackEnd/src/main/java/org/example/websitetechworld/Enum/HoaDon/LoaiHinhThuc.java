package org.example.websitetechworld.Enum.HoaDon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LoaiHinhThuc {
    TIEN_MAT("Tiền mặt"),
    NGAN_HANG("Ngân hàng");

    private final String displayName;

    LoaiHinhThuc(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @JsonValue
    public String toValue() {
        return name(); // Trả về "TIEN_MAT" hoặc "NGAN_HANG"
    }

    // Ánh xạ chuỗi từ JSON sang enum
    @JsonCreator
    public static LoaiHinhThuc fromValue(String value) {
        try {
            return LoaiHinhThuc.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Hình thức thanh toán không hợp lệ: " + value);
        }
    }
}
