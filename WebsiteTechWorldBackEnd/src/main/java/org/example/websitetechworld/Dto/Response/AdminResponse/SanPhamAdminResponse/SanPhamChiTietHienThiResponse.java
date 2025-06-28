package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.*;
import org.example.websitetechworld.Entity.HinhAnh;
import org.example.websitetechworld.Entity.SanPhamChiTiet;

import java.math.BigDecimal;
import java.sql.Struct;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SanPhamChiTietHienThiResponse {
    private Integer idSanPhamChiTiet;

    private String maSanPhamChiTiet;

    private String tenSanPham;

    private String mau;

    private String rom;

    private Integer soLuong;

    private BigDecimal giaBan;

    private String url;

    public static SanPhamChiTietHienThiResponse converDto(SanPhamChiTiet sanPhamChiTiet){
        SanPhamChiTietHienThiResponse response = new SanPhamChiTietHienThiResponse();
        response.setIdSanPhamChiTiet(sanPhamChiTiet.getId());
        if (sanPhamChiTiet.getIdSanPham() != null){
            response.setMaSanPhamChiTiet(sanPhamChiTiet.getIdSanPham().getMaSanPham());
            response.setTenSanPham(sanPhamChiTiet.getIdSanPham().getTenSanPham());
        }
        if (sanPhamChiTiet.getIdMau() != null){
            response.setMau(sanPhamChiTiet.getIdMau().getTenMau());
        }
        if (sanPhamChiTiet.getIdRom() != null){
            response.setRom(sanPhamChiTiet.getIdRom().getDungLuong());
        }
        response.setSoLuong(sanPhamChiTiet.getSoLuong());
        response.setGiaBan(sanPhamChiTiet.getGiaBan());
        Set<HinhAnh> hinhAnhs = sanPhamChiTiet.getHinhAnhs();
        if (hinhAnhs != null && !hinhAnhs.isEmpty()) {
            HinhAnh hinhAnhDauTien = hinhAnhs.iterator().next();
            String urlDauTien = hinhAnhDauTien.getUrl();
            response.setUrl(urlDauTien);
        }
//        response.setTenLoai();
        return response;
    }
}
