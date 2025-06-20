package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Entity.HoaDon;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllHoaDonAdminResponse {
    private Integer idHoaDon;
    private String maHoaDon;
    private String tenNguoiMua;
    private String sdtNguoiMua;
    private BigDecimal tongTien;
    private LocalDate ngayTao;
    private String loaiHoaDon;
    private String trangThaiThanhToan;

    public static GetAllHoaDonAdminResponse convertDto(HoaDon hoaDon){
        GetAllHoaDonAdminResponse response = new GetAllHoaDonAdminResponse();
        response.setIdHoaDon(hoaDon.getId());
        response.setMaHoaDon(hoaDon.getMaHoaDon());
        response.setTenNguoiMua(hoaDon.getTenNguoiMua());
        response.setSdtNguoiMua(hoaDon.getSdtNguoiMua());
        response.setTongTien(hoaDon.getTongTien());
        response.setNgayTao(hoaDon.getNgayTaoHoaDon());
        response.setLoaiHoaDon(hoaDon.getLoaiHoaDon() != null ? hoaDon.getLoaiHoaDon().getDisplayName() : null);
        response.setTrangThaiThanhToan(hoaDon.getTrangThaiThanhToan() != null ? hoaDon.getTrangThaiThanhToan().getDisplayName() : null);
        return  response;
    }
}
