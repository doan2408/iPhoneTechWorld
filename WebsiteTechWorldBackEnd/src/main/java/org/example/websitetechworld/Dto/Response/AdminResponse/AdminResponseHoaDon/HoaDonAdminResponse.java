package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.*;
import org.example.websitetechworld.Entity.HoaDon;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HoaDonAdminResponse {
    private Integer idHoaDon;

    private String maHoaDon;

    private Integer idKhachHang;
    private String maKhachHang;
    private String tenKhachHang; //ten tai khoan

    private Integer idPhieuGiamGia;
    private String maPhieuGiamGia;
    private String tenPhieuGiamGia;

    private String tenNguoiMua;
    private String tenNguoiNhan;

    private String sdtNguoiMua;

    private BigDecimal phiShip;

    private BigDecimal tongTien;

    private BigDecimal soTienGiam;

    private BigDecimal thanhTien;

    private LocalDate ngayTao;

    private String loaiHoaDon;

    private LocalDate ngayThanhToan;

    private String trangThaiThanhToan;

    private List<ChiTietHoaDonAdminResponse> chiTietHoaDonAdminResponseList;
//    private List<ChiTietThanhToanAdminResponse> chiTietThanhToanAdminResponseList;
//    private List<LichSuHoaDonAdminResponse> lichSuHoaDonAdminResponseList;

    public static HoaDonAdminResponse convertDto(HoaDon hoaDon) {
        HoaDonAdminResponse hoaDonAdminResponse = new HoaDonAdminResponse();
        hoaDonAdminResponse.setIdHoaDon(hoaDon.getId());
        hoaDonAdminResponse.setMaHoaDon(hoaDon.getMaHoaDon());
        if (hoaDon.getIdKhachHang() != null){
            hoaDonAdminResponse.setIdKhachHang(hoaDon.getIdKhachHang().getId());
            hoaDonAdminResponse.setMaKhachHang(hoaDon.getIdKhachHang().getMaKhachHang());
            hoaDonAdminResponse.setTenKhachHang(hoaDon.getIdKhachHang().getTenKhachHang());
        }
        if (hoaDon.getIdPhieuGiamGia() != null){
            hoaDonAdminResponse.setIdPhieuGiamGia(hoaDon.getIdPhieuGiamGia().getId());
            hoaDonAdminResponse.setMaPhieuGiamGia(hoaDon.getIdPhieuGiamGia().getMaGiamGia());
            hoaDonAdminResponse.setTenPhieuGiamGia(hoaDon.getIdPhieuGiamGia().getTenKhuyenMai());
        }
        hoaDonAdminResponse.setTenNguoiMua(hoaDon.getTenNguoiMua());
        hoaDonAdminResponse.setTenNguoiNhan(hoaDon.getTenNguoiNhan());
//        hoaDonAdminResponse.setDiaChi(hoaDon.getDiaChi());
        hoaDonAdminResponse.setTenNguoiMua(hoaDon.getTenNguoiMua());
        hoaDonAdminResponse.setSdtNguoiMua(hoaDon.getSdtNguoiMua());
        hoaDonAdminResponse.setPhiShip(hoaDon.getPhiShip());
        hoaDonAdminResponse.setTongTien(hoaDon.getTongTien());
        hoaDonAdminResponse.setSoTienGiam(hoaDon.getSoTienGiam());
        hoaDonAdminResponse.setThanhTien(hoaDon.getThanhTien());
        hoaDonAdminResponse.setNgayTao(hoaDon.getNgayTaoHoaDon());
        hoaDonAdminResponse.setLoaiHoaDon(hoaDon.getLoaiHoaDon() != null ? hoaDon.getLoaiHoaDon().getDisplayName() : null);
        hoaDonAdminResponse.setNgayThanhToan(hoaDon.getNgayThanhToan());
        hoaDonAdminResponse.setTrangThaiThanhToan(hoaDon.getTrangThaiThanhToan() != null ? hoaDon.getTrangThaiThanhToan().getDisplayName() : null );
        if (hoaDon.getChiTietHoaDons() != null){
            hoaDonAdminResponse.setChiTietHoaDonAdminResponseList(hoaDon.getChiTietHoaDons().stream()
                    .map(ChiTietHoaDonAdminResponse::convertDto).collect(Collectors.toList()));
        }
//        if (hoaDon.getChiTietThanhToans() != null){
//            hoaDonAdminResponse.setChiTietThanhToanAdminResponseList(hoaDon.getChiTietThanhToans().stream()
//                    .map(ChiTietThanhToanAdminResponse::convertDto).collect(Collectors.toList()));
//        }
//        if (hoaDon.getLichSuHoaDons() != null){
//            hoaDonAdminResponse.setLichSuHoaDonAdminResponseList(hoaDon.getLichSuHoaDons().stream()
//                    .map(LichSuHoaDonAdminResponse::convertDto).toList());
//        }
        return hoaDonAdminResponse;
    }
}
