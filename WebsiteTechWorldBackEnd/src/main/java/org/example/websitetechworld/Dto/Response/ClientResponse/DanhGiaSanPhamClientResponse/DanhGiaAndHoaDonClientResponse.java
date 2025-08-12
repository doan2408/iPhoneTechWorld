package org.example.websitetechworld.Dto.Response.ClientResponse.DanhGiaSanPhamClientResponse;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.MediaDanhGiaClientResponse.MediaReviewClientResponse;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiDanhGia;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DanhGiaAndHoaDonClientResponse {
    private Integer idDanhGia;
    private Integer idHoaDon;
    private Integer idSanPhamChiTiet;
    private Integer idChiTietHoaDon;
    private Integer idKhachHang;
    private Integer soSao;
    private String noiDung;
    @Enumerated(EnumType.STRING)
    private TrangThaiDanhGia trangThaiDanhGia;
    private List<MediaReviewClientResponse> reviewClientResponsesList;
}
