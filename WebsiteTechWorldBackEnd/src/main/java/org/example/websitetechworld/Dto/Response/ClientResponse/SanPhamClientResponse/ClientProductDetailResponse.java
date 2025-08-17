package org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientProductDetailResponse {
    private Integer idSpct;  //id spct
    private String tenSanPham;
    private List<String> hinhAnh;
    private BigDecimal giaBan;
    private BigDecimal giaTruocKhuyenMai;
    private List<ThuocTinh> rom;
    private List<ThuocTinh> mau;
    private Integer soLuong;
    private Integer tongSoLuong;
    private ThongSoResponse thongSoResponse;
    private String maXuatXu;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ThuocTinh {
        private Integer id;
        private String ten;
    }

}
