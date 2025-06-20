package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewGiaoHangAdminResponse {
    private Integer idGiaoHang;

    private Integer idKhachHang;
    private String maKhachHang;
    private String tenKhachHang;

    private Integer idHoaDon;
    private String maHoaDon;
    private String sdt;
    private String trangThaiThanhToan;

    private String maGiaoHang;

    private LocalDate ngayDatHang;

    private BigDecimal tongGiaTriDonHang;

    private String diaChiGiaoHang;
    private String trangThaiDonHang;

    private List<ChiTietGiaoHangResponseAdmin> chiTietGiaoHangResponseAdminList;

//    public static ViewGiaoHangAdminResponse convertDto(GiaoHang giaoHang){
//        ViewGiaoHangAdminResponse response = new ViewGiaoHangAdminResponse();
//        response.setIdGiaoHang(giaoHang.getId());
//        if (giaoHang.getIdKhachHang() != null){
//            response.setIdKhachHang(giaoHang.getIdKhachHang().getId());
//            response.setMaKhachHang(giaoHang.getIdKhachHang().getMaKhachHang());
//            response.setTenKhachHang(giaoHang.getIdKhachHang().getTenKhachHang());
//        }
//        if (giaoHang.getIdHoaDon() != null){
//            response.setIdHoaDon(giaoHang.getIdHoaDon().getId());
//            response.setMaHoaDon(giaoHang.getIdHoaDon().getMaHoaDon());
//        }
//        response.setMaGiaoHang(giaoHang.getMaGiaoHang());
//        response.setNgayDatHang(giaoHang.getNgayDatHang());
//        response.setTongGiaTriDonHang(giaoHang.getTongGiaTriDonHang());
//        response.setDiaChiGiaoHang(giaoHang.getDiaChiGiaoHang());
//        response.setTrangThaiDonHang(giaoHang.getTrangThaiDonHang()!= null ?giaoHang.getTrangThaiDonHang().getDisplayName() : null);
//        if (giaoHang.getChiTietGiaoHangs() != null){
//            response.setChiTietGiaoHangResponseAdminList(giaoHang.getChiTietGiaoHangs().stream()
//                    .map(ChiTietGiaoHangResponseAdmin::convertDto).toList());
//        }
//        return response;
//    }
}
