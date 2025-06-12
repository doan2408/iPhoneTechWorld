package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietGiaoHangResponseAdmin {
    private Integer id;

    private Integer idSanPhamChiTiet;
    private String maSanPhamChiTiet;
    private String tenSanPham;
    private String maChiTietGiaoHang;

    private Integer soLuong;

    private BigDecimal donGia;

//    public static ChiTietGiaoHangResponseAdmin convertDto(ChiTietGiaoHang chiTietGiaoHang){
//        ChiTietGiaoHangResponseAdmin response = new ChiTietGiaoHangResponseAdmin();
//        response.setId(chiTietGiaoHang.getId());
//        response.setIdSanPhamChiTiet(chiTietGiaoHang.getIdSanPhamChiTiet().getId());
//        response.setMaSanPhamChiTiet(chiTietGiaoHang.getIdSanPhamChiTiet().getMaSanPhamChiTiet());
//        response.setTenSanPham(chiTietGiaoHang.getIdSanPhamChiTiet().getIdSanPham().getTenSanPham());
//        response.setMaChiTietGiaoHang(chiTietGiaoHang.getMaChiTietGiaoHang());
//        response.setSoLuong(chiTietGiaoHang.getSoLuong());
//        response.setDonGia(chiTietGiaoHang.getDonGia());
//        return response;
//    }
}
