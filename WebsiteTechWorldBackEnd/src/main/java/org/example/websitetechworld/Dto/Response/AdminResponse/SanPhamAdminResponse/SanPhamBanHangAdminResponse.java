package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.*;
import org.example.websitetechworld.Entity.HinhAnh;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Entity.SanPhamChiTiet;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SanPhamBanHangAdminResponse {
    private Integer id;

    private String maSanPham;

    private String tenSanPham;

    private String mau;

    private String rom;

    private String url;

    private Integer soLuong;

    private BigDecimal giaBan;


    public static SanPhamBanHangAdminResponse converDto(SanPhamChiTiet sanPhamChiTiet){
        SanPhamBanHangAdminResponse response = new SanPhamBanHangAdminResponse();
        response.setId(sanPhamChiTiet.getId());
        if (sanPhamChiTiet.getIdSanPham() != null){
            response.setMaSanPham(sanPhamChiTiet.getIdSanPham().getMaSanPham());
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
        return response;
    }

}
