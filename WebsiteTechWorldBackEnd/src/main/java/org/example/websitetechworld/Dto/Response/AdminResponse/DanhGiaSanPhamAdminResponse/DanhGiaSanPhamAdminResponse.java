package org.example.websitetechworld.Dto.Response.AdminResponse.DanhGiaSanPhamAdminResponse;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiDanhGia;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class DanhGiaSanPhamAdminResponse {

    private Integer idDanhGia;
    private String tenKhachHang;
    private String maSanPhamChiTiet;
    private String tenSanPham;
    private int soSao;
    private String noiDung;
    private LocalDateTime ngayDanhGia;
    @Enumerated(EnumType.STRING)
    private TrangThaiDanhGia trangThaiDanhGia;
    private List<String> anhUrls;
    private List<String> videoUrls;

}
