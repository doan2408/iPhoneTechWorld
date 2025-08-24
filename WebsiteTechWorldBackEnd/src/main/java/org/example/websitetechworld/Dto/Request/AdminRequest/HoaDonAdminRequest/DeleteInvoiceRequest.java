package org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteInvoiceRequest {
    private String tenNguoiNhan;

    private String sdtNguoiNhan;

    private String emailNguoiNhan;

    private String diaChiGiaoHang;
    private String shippingMethod;

    private BigDecimal phiShip;
}
