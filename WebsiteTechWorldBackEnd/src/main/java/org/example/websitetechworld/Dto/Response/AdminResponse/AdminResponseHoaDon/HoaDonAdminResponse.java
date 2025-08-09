package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.*;
import org.example.websitetechworld.Entity.HoaDon;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private String maVanDon;

    private Integer idKhachHang;
    private String maKhachHang;
    private String tenKhachHang; //ten tai khoan

    private Integer idPhieuGiamGia;
    private String maPhieuGiamGia;
    private String tenPhieuGiamGia;

    private String tenNguoiMua;
    private String tenNguoiNhan;

    private String sdtNguoiMua;
    private String sdtNguoiNhan;

    private BigDecimal phiShip;

    private BigDecimal tongTien;

    private BigDecimal soTienGiam;

    private BigDecimal thanhTien;

    private LocalDateTime ngayTao;
    private LocalDateTime ngayDatHang;

    private String diaChiGiaoHang;
    private String shippingMethod;

    private String loaiHoaDon;
    private Boolean isShipping;
    private LocalDateTime ngayThanhToan;
    private String trangThaiDonHang;
    private String trangThaiThanhToan;
    private Integer idTinhThanh;

    private List<ChiTietHoaDonAdminResponse> chiTietHoaDonAdminResponseList;
//    private List<ChiTietThanhToanAdminResponse> chiTietThanhToanAdminResponseList;
//    private List<LichSuHoaDonAdminResponse> lichSuHoaDonAdminResponseList;

    public static HoaDonAdminResponse convertDto(HoaDon hoaDon) {
        HoaDonAdminResponse hoaDonAdminResponse = new HoaDonAdminResponse();
        hoaDonAdminResponse.setIdHoaDon(hoaDon.getId());
        hoaDonAdminResponse.setMaHoaDon(hoaDon.getMaHoaDon());
        hoaDonAdminResponse.setMaVanDon(hoaDon.getMaVanDon());
        if (hoaDon.getIdKhachHang() != null){
            hoaDonAdminResponse.setIdKhachHang(hoaDon.getIdKhachHang().getId());
            hoaDonAdminResponse.setMaKhachHang(hoaDon.getIdKhachHang().getMaKhachHang());
            hoaDonAdminResponse.setTenKhachHang(hoaDon.getIdKhachHang().getTenKhachHang());
        }
        if (hoaDon.getIdPhieuGiamGia() != null){
            hoaDonAdminResponse.setIdPhieuGiamGia(hoaDon.getIdPhieuGiamGia().getId());
            hoaDonAdminResponse.setMaPhieuGiamGia(hoaDon.getIdPhieuGiamGia().getMaGiamGia());
            hoaDonAdminResponse.setTenPhieuGiamGia(hoaDon.getIdPhieuGiamGia().getTenGiamGia());
        }

        hoaDonAdminResponse.setTenNguoiMua(hoaDon.getTenNguoiMua());
        hoaDonAdminResponse.setTenNguoiNhan(hoaDon.getTenNguoiNhan());
        hoaDonAdminResponse.setDiaChiGiaoHang(hoaDon.getDiaChiGiaoHang());
        hoaDonAdminResponse.setTenNguoiMua(hoaDon.getTenNguoiMua());
        hoaDonAdminResponse.setSdtNguoiMua(hoaDon.getSdtNguoiMua());
        hoaDonAdminResponse.setSdtNguoiNhan(hoaDon.getSdtNguoiNhan());
        hoaDonAdminResponse.setPhiShip(hoaDon.getPhiShip());
        hoaDonAdminResponse.setTongTien(hoaDon.getTongTien());
        hoaDonAdminResponse.setSoTienGiam(hoaDon.getSoTienGiam());
        hoaDonAdminResponse.setThanhTien(hoaDon.getThanhTien());
        hoaDonAdminResponse.setNgayTao(hoaDon.getNgayTaoHoaDon());
        hoaDonAdminResponse.setNgayDatHang(hoaDon.getNgayDatHang());
        hoaDonAdminResponse.setIsShipping(hoaDon.getIsShipping());
        hoaDonAdminResponse.setLoaiHoaDon(hoaDon.getLoaiHoaDon() != null ? hoaDon.getLoaiHoaDon().getDisplayName() : null);
        hoaDonAdminResponse.setNgayThanhToan(hoaDon.getNgayThanhToan());
        hoaDonAdminResponse.setTrangThaiThanhToan(hoaDon.getTrangThaiThanhToan() != null ? hoaDon.getTrangThaiThanhToan().getDisplayName() : null );
        hoaDonAdminResponse.setTrangThaiDonHang(hoaDon.getTrangThaiDonHang() != null ? hoaDon.getTrangThaiDonHang().getDisplayName() : null );
        hoaDonAdminResponse.setShippingMethod(hoaDon.getShippingMethod() != null ? hoaDon.getShippingMethod().getCode() : null);
        if (hoaDon.getChiTietHoaDons() != null){
            hoaDonAdminResponse.setChiTietHoaDonAdminResponseList(hoaDon.getChiTietHoaDons().stream()
                    .map(ChiTietHoaDonAdminResponse::convertDto).collect(Collectors.toList()));
        }
        return hoaDonAdminResponse;
    }
}
