package org.example.websitetechworld.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
    private String tenNguoiNhan;
    private String sdtNguoiNhan;
    private String diaChiGiaoHang;
    private String shippingMethod;
    private BigDecimal phiShip;
    private boolean isShipping;
    private String maVanDon;
}
