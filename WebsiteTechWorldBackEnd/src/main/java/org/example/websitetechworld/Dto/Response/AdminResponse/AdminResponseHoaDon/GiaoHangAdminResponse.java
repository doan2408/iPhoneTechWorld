package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Entity.GiaoHang;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GiaoHangAdminResponse {
    private Integer idGiaoHang;

    private Integer idKhachHang;
    private String maKhachHang;
    private String tenKhachHang;

    private Integer idHoaDon;
    private String maHoaDon;

    private String maGiaoHang;

    private LocalDate ngayDatHang;

    private BigDecimal tongGiaTriDonHang;

    private String diaChiGiaoHang;

    private String trangThaiDonHang;

    public static GiaoHangAdminResponse convertDto(GiaoHang giaoHang){
        GiaoHangAdminResponse response = new GiaoHangAdminResponse();
        response.setIdGiaoHang(giaoHang.getId());
        if (giaoHang.getIdKhachHang() != null){
            response.setIdKhachHang(giaoHang.getIdKhachHang().getId());
            response.setMaKhachHang(giaoHang.getIdKhachHang().getMaKhachHang());
            response.setTenKhachHang(giaoHang.getIdKhachHang().getTenKhachHang());
        }
        if (giaoHang.getIdHoaDon() != null){
            response.setIdHoaDon(giaoHang.getIdHoaDon().getId());
            response.setMaHoaDon(giaoHang.getIdHoaDon().getMaHoaDon());
        }
        response.setMaGiaoHang(giaoHang.getMaGiaoHang());
        response.setNgayDatHang(giaoHang.getNgayDatHang());
        response.setTongGiaTriDonHang(giaoHang.getTongGiaTriDonHang());
        response.setDiaChiGiaoHang(giaoHang.getDiaChiGiaoHang());
        response.setTrangThaiDonHang(giaoHang.getTrangThaiDonHang()!= null ?giaoHang.getTrangThaiDonHang().name() : null);
        return response;
    }


}
