package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Entity.HinhAnh;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Entity.SanPhamChiTiet;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamBanHangAdminResponse {
    private Integer id;

    private String tenSanPham;

    private String hinhHanh;

    private Integer soLuong;

    private BigDecimal gia;




    public static SanPhamBanHangAdminResponse converDto(SanPhamChiTiet sanPhamChiTiet){
        SanPhamBanHangAdminResponse response = new SanPhamBanHangAdminResponse();
        response.setId(sanPhamChiTiet.getId());
        if (sanPhamChiTiet.getIdSanPham() != null){
            response.setTenSanPham(sanPhamChiTiet.getIdSanPham().getTenSanPham());
        }
        Set<HinhAnh> hinhAnhs = sanPhamChiTiet.getHinhAnhs();
        if (hinhAnhs != null && !hinhAnhs.isEmpty()) {
            HinhAnh hinhAnhDauTien = hinhAnhs.iterator().next();
            String urlDauTien = hinhAnhDauTien.getUrl();
            response.setHinhHanh(urlDauTien);
        }
        response.setSoLuong(sanPhamChiTiet.getSoLuong());
        response.setGia(sanPhamChiTiet.getGiaBan());
        return response;
    }

}
