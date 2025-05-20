package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Entity.LichSuHoaDon;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LichSuHoaDonAdminResponse {
    private Integer idLichSuHoaDon;

    private Integer idNhanVien;
    private String maNhanVien;
    private String tenNhanVien;

    private Integer idHoaDon;
    private String maHoaDon;

    private String hanhDong;

    private LocalDate thoiGianThayDoi;

    private String moTa;

    public static LichSuHoaDonAdminResponse convertDto(LichSuHoaDon lichSuHoaDon) {
        LichSuHoaDonAdminResponse response = new LichSuHoaDonAdminResponse();
        response.setIdLichSuHoaDon(lichSuHoaDon.getId());
        if (lichSuHoaDon.getIdNhanVien() != null){
            response.setIdNhanVien(lichSuHoaDon.getIdNhanVien().getId());
            response.setMaNhanVien(lichSuHoaDon.getIdNhanVien().getMaNhanVien());
            response.setTenNhanVien(lichSuHoaDon.getIdNhanVien().getTenNhanVien());
        }
        if (lichSuHoaDon.getIdHoaDon() != null){
            response.setIdHoaDon(lichSuHoaDon.getIdHoaDon().getId());
            response.setMaHoaDon(lichSuHoaDon.getIdHoaDon().getMaHoaDon());
        }
        response.setHanhDong(lichSuHoaDon.getHanhDong());
        response.setThoiGianThayDoi(lichSuHoaDon.getThoiGianThayDoi());
        response.setMoTa(lichSuHoaDon.getMoTa());

        return response;
    }
}
