package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "chi_tiet_hoa_don")
public class ChiTietHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chi_tiet_hoa_don", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_hoa_don")
    private HoaDon idHoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_san_pham_chi_tiet")
    private SanPhamChiTiet idSanPhamChiTiet;

    @Size(max = 36)
    @Column(name = "ma_chi_tiet_hoa_don", length = 36,insertable = false, updatable = false)
    private String maChiTietHoaDon;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_san_pham", length = 50)
    private String tenSanPham;

    @Size(max = 100)
    @Nationalized
    @Column(name = "mo_ta", length = 100)
    private String moTa;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "don_gia", precision = 10, scale = 2)
    private BigDecimal donGia;

    @OneToMany(mappedBy = "idHoaDonChiTiet",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ImeiDaBan> imeiDaBans = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idChiTietHoaDon", cascade = {CascadeType.PERSIST , CascadeType.MERGE})
    private List<DanhGiaSanPham> danhGiaSanPhams = new ArrayList<>();

}