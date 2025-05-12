package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.*;
import org.example.websitetechworld.Entity.HoaDon;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private String tenKhachHang;

    private Integer idPhieuGiamGia;
    private String maPhieuGiamGia;
    private String tenPhieuGiamGia;

    private String tenNguoiNhan;

    private String diaChi;

    private String sdt;

    private BigDecimal phiShip;

    private BigDecimal tongTien;

    private BigDecimal soTienGiam;

    private BigDecimal thanhTien;

    private LocalDate ngayTao;

    private String loaiHoaDon;

    private LocalDate ngayThanhToan;

    private String trangThaiThanhToan;

    public static HoaDonAdminResponse convertDto(HoaDon hoaDon) {
        HoaDonAdminResponse hoaDonAdminResponse = new HoaDonAdminResponse();
        hoaDonAdminResponse.setIdHoaDon(hoaDon.getId());
        hoaDonAdminResponse.setMaHoaDon(hoaDon.getMaHoaDon());
        hoaDonAdminResponse.setIdKhachHang(hoaDon.getIdKhachHang().getId());
        hoaDonAdminResponse.setMaKhachHang(hoaDon.getIdKhachHang().getMaKhachHang());
        hoaDonAdminResponse.setTenKhachHang(hoaDon.getIdKhachHang().getTenKhachHang());
        hoaDonAdminResponse.setIdPhieuGiamGia(hoaDon.getIdPhieuGiamGia().getId());
        hoaDonAdminResponse.setMaPhieuGiamGia(hoaDon.getIdPhieuGiamGia().getMaGiamGia());
        hoaDonAdminResponse.setTenPhieuGiamGia(hoaDon.getIdPhieuGiamGia().getTenKhuyenMai());
        hoaDonAdminResponse.setTenNguoiNhan(hoaDon.getTenNguoiNhan());
        hoaDonAdminResponse.setDiaChi(hoaDon.getDiaChi());
        hoaDonAdminResponse.setSdt(hoaDon.getSdt());
        hoaDonAdminResponse.setPhiShip(hoaDon.getPhiShip());
        hoaDonAdminResponse.setTongTien(hoaDon.getTongTien());
        hoaDonAdminResponse.setSoTienGiam(hoaDon.getSoTienGiam());
        hoaDonAdminResponse.setThanhTien(hoaDon.getThanhTien());
        hoaDonAdminResponse.setNgayTao(hoaDon.getNgayTao());
        hoaDonAdminResponse.setLoaiHoaDon(hoaDon.getLoaiHoaDon() != null ? hoaDon.getLoaiHoaDon().name() : null);
        hoaDonAdminResponse.setNgayThanhToan(hoaDon.getNgayThanhToan());
        hoaDonAdminResponse.setTrangThaiThanhToan(hoaDon.getTrangThaiThanhToan() != null ? hoaDon.getTrangThaiThanhToan().name() : null );
        return hoaDonAdminResponse;
    }
}
