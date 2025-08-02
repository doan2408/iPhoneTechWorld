package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@Table(name = "san_pham_chi_tiet")
@ToString
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_san_pham_chi_tiet", nullable = false)
    private Integer id;

    @Size(max = 36)
    @Column(name = "ma_san_pham_chi_tiet", length = 36, insertable = false, updatable = false)
    private String maSanPhamChiTiet;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_san_pham")
    private SanPham idSanPham;

    //chitiet
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mau")
    private MauSac idMau;

    //chitiet
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rom")
    private Rom idRom;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "gia_ban", precision = 10, scale = 2)
    private BigDecimal giaBan;

    @OneToMany(mappedBy = "idSanPhamChiTiet",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<BaoHanh> baoHanhs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSanPhamChiTiet",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<ChiTietHoaDon> chiTietHoaDons = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSanPhamChiTiet",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<GioHangChiTiet> gioHangChiTiets = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSanPhamChiTiet",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<HinhAnh> hinhAnhs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSanPhamChiTiet",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Imei> imeis = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSanPhamChiTiet", cascade = {CascadeType.PERSIST , CascadeType.MERGE})
    private List<DanhGiaSanPham> danhGiaSanPhams = new ArrayList<>();

}