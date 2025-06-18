package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Entity.NhaCungCap;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPham;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SanPhamChiTietResponse {

    private Integer id;
    private String maSanPhamChiTiet;
    private Integer soLuongSPCT;
    private BigDecimal giaBan;


    private String maSanPham;
    private String tenSanPham;
    private String thuongHieu;
    private TrangThaiSanPham trangThaiSanPham;
//    private Integer soLuongTonKho;

    private String tenNhaCungCap;
    private String diaChi;
    private String sdt;
    private String email;

    private String tenMau;
    private String maMau;

    private String dungLuongRam;
    private String loaiRam;
    private String tocDoDocGhiRam;
    private String nhaSanXuatRam;
    private LocalDate namRaMatRam;

    private String dungLuongRom;
    private String loaiRom;
    private String tocDoDocGhiRom;
    private String nhaSanXuatRom;
    private LocalDate namRaMatRom;

    private String tenManHinh;
    private String kichThuoc;
    private String loaiManHinh;
    private String doPhanGiaiManHinh;
    private String tanSoQuet;
    private String doSang;
    private String chatLieuKinh;

    private String PhienBanHeDieuHanh;
    private String nhaPhatTrien;
    private String giaoDienNguoiDung;

    private String phienBanPin;
    private String congSuatSac;
    private String thoiGianSuDung;
    private String soLanSacToiDa;

    //Cpu
    private String hangSanXuat;
    private String soNhan;
    private Integer soLuongCpu;
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

    private Set<ImeiAdminResponse> imeis;

    private Set<HinhAnhAdminResponse> hinhAnhs;

}
