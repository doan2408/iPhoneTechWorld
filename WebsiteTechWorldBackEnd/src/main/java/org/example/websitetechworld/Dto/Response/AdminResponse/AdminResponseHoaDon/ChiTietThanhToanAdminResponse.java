package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Entity.ChiTietThanhToan;


import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietThanhToanAdminResponse {
    private Integer idChiTietThanhToan;

    private Integer idHoaDon;
    private String maHoaDon;

    private Integer idPhuongThucThanhToan;
    private String tenPhuongThuc;
    private String loaiHinhThuc;

    private BigDecimal soTienThanhToan;

    public static ChiTietThanhToanAdminResponse convertDto(ChiTietThanhToan chiTietThanhToan){
        ChiTietThanhToanAdminResponse response = new ChiTietThanhToanAdminResponse();
        response.setIdChiTietThanhToan(chiTietThanhToan.getId());
        if (chiTietThanhToan.getIdHoaDon() != null){
            response.setIdHoaDon(chiTietThanhToan.getIdHoaDon().getId());
            response.setMaHoaDon(chiTietThanhToan.getIdHoaDon().getMaHoaDon());
        }
        if (chiTietThanhToan.getIdPhuongThucThanhToan() != null){
            response.setIdPhuongThucThanhToan(chiTietThanhToan.getId());
            response.setTenPhuongThuc(chiTietThanhToan.getIdPhuongThucThanhToan().getTenPhuongThuc().getDisplayName());
            response.setLoaiHinhThuc(chiTietThanhToan.getIdPhuongThucThanhToan().getLoaiHinhThuc());
        }
        response.setSoTienThanhToan(chiTietThanhToan.getSoTienThanhToan());
        return response;
    }

}
