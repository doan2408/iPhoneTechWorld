package org.example.websitetechworld.Dto.Response.ClientResponse.GioHangClientResponse;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GioHangClientResponse {

    private Integer idGioHang;
    private Integer idKhachHang;
    private List<GioHangChiTietResponse> items;

    @Data
    public static class GioHangChiTietResponse {
        private Integer idGioHangChiTiet;
        private Integer idSanPhamChiTiet;
        private String tenSanPham;
        private String phienBan;
        private String imageUrl;
        private BigDecimal gia;
        private Integer soLuong;
        private Integer soLuongTon;
        private LocalDateTime ngayThem;
    }
}
