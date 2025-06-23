package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPham;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelSanPhamAdminResponse {
    private Integer id_model_san_pham;

    private String ma_model_san_pham;
    private String ten_model;
    private Date nam_ra_mat;
    private String mo_ta;
    private String trang_thai;

    // san pham
    private String maSanPham;
    private String tenSanPham;
    private String thuongHieu;
    @Enumerated(EnumType.STRING)
    private TrangThaiSanPham trangThaiSanPham;

    //nha cung cap
    private String tenNhaCungCap;
    private String diaChi;
    private String sdt;
    private String email;

    private String dungLuongRam;
    private String loaiRam;
    private String tocDoDocGhiRam;
    private String nhaSanXuatRam;
    private LocalDate namRaMatRam;

    private String tenManHinh;
    private String kichThuoc;
    private String loaiManHinh;
    private String doPhanGiaiManHinh;
    private String tanSoQuet;
    private String doSang;
    private String chatLieuKinh;

    private String phienBanHeDieuHanh;
    private String nhaPhatTrien;
    private String giaoDienNguoiDung;

    private String phienBanPin;
    private String congSuatSac;
    private String thoiGianSuDung;
    private String soLanSacToiDa;

//    Cpu
    private String hangSanXuat;
    private String soNhan;
    private String chipXuLy;
    private String xungNhip;
    private String congNgheSanXuat;
    private String boNhoDem;
    private String tieuThuDienNang;
    private LocalDate namRaMat;

    private String loaiCameraTruoc;
    private String doPhanGiaiCameraTruoc;
    private String khauDoCameraTruoc;
    private String loaiZoomCameraTruoc;
    private String cheDoChupCameraTruoc;

    private String loaiCameraSau;
    private String doPhanGiaiCameraSau;
    private String khauDoCameraSau;
    private String loaiZoomCameraSau;
    private String cheDoChupCameraSau;

    private String maXuatXu;
    private String tenQuocGia;

    private String tenLoai;
}
