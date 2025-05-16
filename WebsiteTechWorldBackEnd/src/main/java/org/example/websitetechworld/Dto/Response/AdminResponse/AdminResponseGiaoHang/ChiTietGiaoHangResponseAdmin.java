package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Entity.ChiTietGiaoHang;
import org.example.websitetechworld.Entity.GiaoHang;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietGiaoHangResponseAdmin {
    private Integer id;

    private Integer idSanPhamChiTiet;
    private String maSanPhamChiTiet;

    private String maChiTietGiaoHang;

    private Integer soLuong;

    private BigDecimal donGia;

    public static ChiTietGiaoHangResponseAdmin convertDto(ChiTietGiaoHang chiTietGiaoHang){
        ChiTietGiaoHangResponseAdmin response = new ChiTietGiaoHangResponseAdmin();
        response.setId(chiTietGiaoHang.getId());
        response.setIdSanPhamChiTiet(chiTietGiaoHang.getIdSanPhamChiTiet().getId());
        response.setMaSanPhamChiTiet(chiTietGiaoHang.getIdSanPhamChiTiet().getMaSanPhamChiTiet());
        response.setMaChiTietGiaoHang(chiTietGiaoHang.getMaChiTietGiaoHang());
        response.setSoLuong(chiTietGiaoHang.getSoLuong());
        response.setDonGia(chiTietGiaoHang.getDonGia());
        return response;
    }
}
