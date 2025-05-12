package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.*;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.SanPhamChiTiet;


import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChiTietHoaDonAdminResponse {
    private Integer id;

    private HoaDon idHoaDon;

    private Integer idSanPhamChiTiet;
    private String maSanPhamChiTiet;

    private String maChiTietHoaDon;

    private String tenSanPham;

    private String moTa;

    private Integer soLuong;

    private BigDecimal donGia;
}
