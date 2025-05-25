package org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietHoaDonAdminRequest {
    private Integer idHoaDon;

    private Integer idSanPhamChiTiet;

    private String tenSanPham;

    private String moTa;

    private Integer soLuong;

//    private BigDecimal donGia;

}
