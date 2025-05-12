package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.*;
import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.SanPhamChiTiet;


import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChiTietHoaDonAdminResponse {
    private Integer idHoaDonChiTiet;

    private Integer idHoaDon;
    private String maHoaDon;

    private Integer idSanPhamChiTiet;
    private String maSanPhamChiTiet;

    private String maChiTietHoaDon;

    private String tenSanPham;

    private String moTa;

    private Integer soLuong;

    private BigDecimal donGia;

    public static ChiTietHoaDonAdminResponse convertDto(ChiTietHoaDon chiTietHoaDon){
        ChiTietHoaDonAdminResponse chiTietHoaDonAdminResponse = new ChiTietHoaDonAdminResponse();
        if (chiTietHoaDon.getIdHoaDon() != null){
            chiTietHoaDonAdminResponse.setIdHoaDonChiTiet(chiTietHoaDon.getId());
            chiTietHoaDonAdminResponse.setIdHoaDon(chiTietHoaDonAdminResponse.getIdHoaDon());
        }
        if (chiTietHoaDon.getIdSanPhamChiTiet() != null){
            chiTietHoaDonAdminResponse.setIdSanPhamChiTiet(chiTietHoaDon.getIdSanPhamChiTiet().getId());
            chiTietHoaDonAdminResponse.setMaChiTietHoaDon(chiTietHoaDon.getIdSanPhamChiTiet().getMaSanPhamChiTiet());
        }
        chiTietHoaDonAdminResponse.setMaChiTietHoaDon(chiTietHoaDon.getMaChiTietHoaDon());
        chiTietHoaDonAdminResponse.setTenSanPham(chiTietHoaDon.getTenSanPham());
        chiTietHoaDonAdminResponse.setMoTa(chiTietHoaDon.getMoTa());
        chiTietHoaDonAdminResponse.setSoLuong(chiTietHoaDon.getSoLuong());
        chiTietHoaDonAdminResponse.setDonGia(chiTietHoaDon.getDonGia());
        return chiTietHoaDonAdminResponse;
    }
}
