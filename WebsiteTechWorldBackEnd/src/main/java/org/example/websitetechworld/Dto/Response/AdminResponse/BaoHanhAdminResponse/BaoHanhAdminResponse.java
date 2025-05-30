package org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.HoaDonAdminResponse;
import org.example.websitetechworld.Entity.BaoHanh;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.KhachHang;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaoHanhAdminResponse {
    private Integer id;

    private Integer idKhachHang;
    private String maKhachHang;
    private String tenKhachHang;

    private Integer idSanPhamChiTiet; // Thay vì sử dụng `SanPhamChiTiet`, bạn có thể chỉ lấy ID
    private String maSanPhamChiTiet;

    private Integer idLoaiBaoHanh; // Thay vì `LoaiBaoHanh`, bạn có thể chỉ lấy ID
    private String tenLoaiBaoHanh;
    private Integer thoiGianThang;


    private LocalDate ngayBatDau;

    private LocalDate ngayKetThuc;


    private String trangThaiBaoHanh;


    public static BaoHanhAdminResponse convertDto(BaoHanh baoHanh) {
        BaoHanhAdminResponse baoHanhAdminResponse = new BaoHanhAdminResponse();
        baoHanhAdminResponse.setId(baoHanh.getId());
        if (baoHanh.getIdKhachHang() != null) {
            baoHanhAdminResponse.setIdKhachHang(baoHanh.getIdKhachHang().getId());
            baoHanhAdminResponse.setMaKhachHang(baoHanh.getIdKhachHang().getMaKhachHang());
            baoHanhAdminResponse.setTenKhachHang(baoHanh.getIdKhachHang().getTenKhachHang());
        }

        if (baoHanh.getIdSanPhamChiTiet() != null) {
            baoHanhAdminResponse.setIdSanPhamChiTiet(baoHanh.getIdSanPhamChiTiet().getId());
            baoHanhAdminResponse.setMaSanPhamChiTiet(baoHanh.getIdSanPhamChiTiet().getMaSanPhamChiTiet());
        }
        if (baoHanh.getIdLoaiBaoHanh() != null) {
            baoHanhAdminResponse.setIdLoaiBaoHanh(baoHanh.getIdLoaiBaoHanh().getId());
            baoHanhAdminResponse.setTenLoaiBaoHanh(baoHanh.getIdLoaiBaoHanh().getTenLoaiBaoHanh());
            baoHanhAdminResponse.setThoiGianThang(baoHanh.getIdLoaiBaoHanh().getThoiGianThang());
        }
        baoHanhAdminResponse.setNgayBatDau(baoHanh.getNgayBatDau());
        baoHanhAdminResponse.setNgayKetThuc(baoHanh.getNgayKetThuc());
        baoHanhAdminResponse.setTrangThaiBaoHanh(baoHanh.getTrangThaiBaoHanh() != null ? baoHanh.getTrangThaiBaoHanh().name() : null);


        return baoHanhAdminResponse;
    }
}
