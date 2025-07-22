package org.example.websitetechworld.Enum.HoaDon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TenHinhThuc {
    TIEN_MAT("Tiền mặt"),
    CHUYEN_KHOAN("Chuyển khoản"),
    NGAN_HANG("Ngân hàng"),
    COD("Thanh toán sau khi nhận hàng"),
    VNPAY("Thanh toán qua VNPAY");

    private final String displayName;

    TenHinhThuc(String displayName) {
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
    public static TenHinhThuc fromValue(String value) {
        try {
            return TenHinhThuc.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Hình thức thanh toán không hợp lệ: " + value);
        }
    }
}
