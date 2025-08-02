package org.example.websitetechworld.Dto.Request.ClientRequest.DanhGiaSanPhamClientRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiDanhGia;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DanhGiaSanPhamClientRequest {
    private Integer soSao;
    private String noiDung;
    private Integer idKhachHang;
    private Integer idSanPhamChiTiet;
    private Integer idChiTietHoaDon;
    private TrangThaiDanhGia trangThaiDanhGia;
}
