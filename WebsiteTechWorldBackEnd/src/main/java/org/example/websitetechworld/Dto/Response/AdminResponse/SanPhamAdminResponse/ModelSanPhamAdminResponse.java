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
    private Integer idModelSanPham;

    private String maModelSanPham;
    private String tenModel;
    private LocalDate namRaMat;
    private String moTa;
    private String trangThai;

    private Integer idRam;
    private String dungLuongRam;
    private String loaiRam;
    private String tocDoDocGhiRam;
    private String nhaSanXuatRam;
    private LocalDate namRaMatRam;

    private Integer idManHinh;
    private String tenManHinh;
    private String kichThuoc;
    private String loaiManHinh;
    private String doPhanGiaiManHinh;
    private String tanSoQuet;
    private String doSang;
    private String chatLieuKinh;

    private Integer idHeDieuHanh;
    private String phienBanHeDieuHanh;
    private String nhaPhatTrien;
    private String giaoDienNguoiDung;

    private Integer idPin;
    private String phienBanPin;
    private String congSuatSac;
    private String thoiGianSuDung;
    private String soLanSacToiDa;

//    Cpu
    private Integer idCpu;
    private String hangSanXuat;
    private String soNhan;
    private String chipXuLy;
    private String xungNhip;
    private String congNgheSanXuat;
    private String boNhoDem;
    private String tieuThuDienNang;
    private LocalDate namRaMatCpu;

    private Integer idCameraTruoc;
    private String loaiCameraTruoc;
    private String doPhanGiaiCameraTruoc;
    private String khauDoCameraTruoc;
    private String loaiZoomCameraTruoc;
    private String cheDoChupCameraTruoc;

    private Integer idCameraSau;
    private String loaiCameraSau;
    private String doPhanGiaiCameraSau;
    private String khauDoCameraSau;
    private String loaiZoomCameraSau;
    private String cheDoChupCameraSau;

    private Integer idXuatXu;
    private String maXuatXu;
    private String tenQuocGia;

    private Integer idLoai;
    private String tenLoai;
}
