package org.example.websitetechworld.Enum.GiaoHang;

public enum ShippingMethod {
    EXPRESS("express", 15000, 3000), // Phí cơ bản 15,000 VNĐ, tăng 3,000 VNĐ/km sau 2km
    STANDARD("standard", 10000, 2000); // Phí cơ bản 10,000 VNĐ, tăng 2,000 VNĐ/km sau 2km

    private final String code; // Giá trị dùng để gửi/receive từ frontend
    private final int baseFee; // Phí cơ bản
    private final int additionalFeePerKm; // Phí bổ sung mỗi km sau 2km

    ShippingMethod(String code, int baseFee, int additionalFeePerKm) {
        this.code = code;
        this.baseFee = baseFee;
        this.additionalFeePerKm = additionalFeePerKm;
    }

    public String getCode() {
        return code;
    }

    public int getBaseFee() {
        return baseFee;
    }

    public int getAdditionalFeePerKm() {
        return additionalFeePerKm;
    }

    // Phương thức tính phí dựa trên khoảng cách
    public int calculateShippingFee(int distanceInKm) {
        if (distanceInKm <= 2) {
            return baseFee;
        }
        return baseFee + (distanceInKm - 2) * additionalFeePerKm;
    }

    // Tìm ShippingMethod từ code (dùng để parse từ frontend)
    public static ShippingMethod fromCode(String code) {
        for (ShippingMethod method : ShippingMethod.values()) {
            if (method.code.equalsIgnoreCase(code)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy phương thức giao hàng với code: " + code);
    }
}
