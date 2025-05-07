package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "chi_tiet_giao_hang")
public class ChiTietGiaoHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chi_tiet_giao_hang", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_giao_hang")
    private GiaoHang idGiaoHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_san_pham_chi_tiet")
    private SanPhamChiTiet idSanPhamChiTiet;

    @Size(max = 36)
    @Column(name = "ma_chi_tiet_giao_hang", length = 36,insertable = false, updatable = false)
    private String maChiTietGiaoHang;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "don_gia", precision = 10, scale = 2)
    private BigDecimal donGia;

}