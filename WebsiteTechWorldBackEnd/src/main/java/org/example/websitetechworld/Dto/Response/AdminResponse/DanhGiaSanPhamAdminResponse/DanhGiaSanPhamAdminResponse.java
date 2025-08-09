package org.example.websitetechworld.Dto.Response.AdminResponse.DanhGiaSanPhamAdminResponse;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiDanhGia;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DanhGiaSanPhamAdminResponse {

    private Integer idDanhGia;
    private String tenKhachHang;
    private String maSanPhamChiTiet;
    private String tenSanPham;
    private String tenMau;
    private String dungLuongRam;
    private String dungLuongRom;
    private Integer soSao;
    private String noiDung;
    private String noiDungPhanHoi;
    private LocalDateTime ngayDanhGia;
    @Enumerated(EnumType.STRING)
    private TrangThaiDanhGia trangThaiDanhGia;
    private List<String> anhUrls = Collections.emptyList(); // Khởi tạo mặc định
    private List<String> videoUrls = Collections.emptyList(); // Khởi tạo mặc định

}
