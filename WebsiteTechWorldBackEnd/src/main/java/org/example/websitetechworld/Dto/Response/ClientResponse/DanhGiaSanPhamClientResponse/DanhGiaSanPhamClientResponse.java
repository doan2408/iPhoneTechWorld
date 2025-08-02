package org.example.websitetechworld.Dto.Response.ClientResponse.DanhGiaSanPhamClientResponse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiDanhGia;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DanhGiaSanPhamClientResponse {
    private Integer idDanhGia;
    private Integer soSao;
    private String noiDung;
    private LocalDateTime ngayDanhGia;
    private TrangThaiDanhGia trangThaiDanhGia;
    private String tenKhachHang;
    private String tenSanPham;
    private Integer idKhachHang;
    private Integer idSanPhamChiTiet;
    private Integer idChiTietHoaDon;
    private List<String> mediaUrls;
    private Integer soPhanHoi;
}
