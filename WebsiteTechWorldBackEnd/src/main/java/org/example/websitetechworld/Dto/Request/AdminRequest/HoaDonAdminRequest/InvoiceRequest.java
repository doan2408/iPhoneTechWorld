package org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
    private String tenNguoiNhan;
    private String sdtNguoiNhan;
    private String emailNguoiNhan;
    private String diaChiGiaoHang;
    private String shippingMethod;
    private BigDecimal phiShip;
    private Boolean isShipping;
    private String maVanDon;
    private BigDecimal thanhTien;
    private LocalDate ngayTaoDonHang;
}
