package org.example.websitetechworld.Enum.HoaDon;

public enum HanhDongLichSuHoaDon {
    CREATE("Tạo hóa đơn ")
    , UPDATE("Cập nhật hóa đơn ")
    , DELETE("Xóa hóa đơn")
    , THANH_TOAN("Thanh toán hóa đơn")
    , HUY("Hủy hóa đơn")
    , RETURN("Trả hàng")
    , FALSE_SHIPPING("Giao thất bại")
    , COMFIRM("Xác nhận hóa đơn")
    , COMPLETE("hOAN THANH");

    private final String displayName;

    HanhDongLichSuHoaDon(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
