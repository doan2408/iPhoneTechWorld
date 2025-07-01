package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.*;
import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.HinhAnh;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.SanPhamChiTiet;


import java.math.BigDecimal;
import java.util.Set;

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
    private String imageSanPham;

    private String maChiTietHoaDon;

    private String tenSanPham;

    private String moTa;

    private Integer soLuong;

    private BigDecimal donGia;

    public static ChiTietHoaDonAdminResponse convertDto(ChiTietHoaDon chiTietHoaDon){
        ChiTietHoaDonAdminResponse chiTietHoaDonAdminResponse = new ChiTietHoaDonAdminResponse();
        chiTietHoaDonAdminResponse.setIdHoaDonChiTiet(chiTietHoaDon.getId());
        if (chiTietHoaDon.getIdHoaDon() != null){
            chiTietHoaDonAdminResponse.setIdHoaDon(chiTietHoaDon.getIdHoaDon().getId());
            chiTietHoaDonAdminResponse.setMaHoaDon(chiTietHoaDon.getIdHoaDon().getMaHoaDon());
        }
        if (chiTietHoaDon.getIdSanPhamChiTiet() != null){
            chiTietHoaDonAdminResponse.setIdSanPhamChiTiet(chiTietHoaDon.getIdSanPhamChiTiet().getId());
            chiTietHoaDonAdminResponse.setMaSanPhamChiTiet(chiTietHoaDon.getIdSanPhamChiTiet().getMaSanPhamChiTiet());

            Set<HinhAnh> hinhAnhs = chiTietHoaDon.getIdSanPhamChiTiet().getHinhAnhs();
            if (hinhAnhs != null && !hinhAnhs.isEmpty()) {
                HinhAnh hinhAnhDauTien = hinhAnhs.iterator().next();
                String urlDauTien = hinhAnhDauTien.getUrl();
                chiTietHoaDonAdminResponse.setImageSanPham(urlDauTien);
            }




        }
        chiTietHoaDonAdminResponse.setMaChiTietHoaDon(chiTietHoaDon.getMaChiTietHoaDon());
        chiTietHoaDonAdminResponse.setTenSanPham(chiTietHoaDon.getTenSanPham());
        chiTietHoaDonAdminResponse.setMoTa(chiTietHoaDon.getMoTa());
        chiTietHoaDonAdminResponse.setSoLuong(chiTietHoaDon.getSoLuong());
        chiTietHoaDonAdminResponse.setDonGia(chiTietHoaDon.getDonGia());
        return chiTietHoaDonAdminResponse;
    }

}
