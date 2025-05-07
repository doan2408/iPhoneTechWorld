package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "phieu_giam_gia")
public class PhieuGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phieu_giam_gia", nullable = false)
    private Integer id;

    @Size(max = 34)
    @ColumnDefault("'VC'+case when [id_phieu_giam_gia]<10 then '00'+CONVERT([varchar],[id_phieu_giam_gia]) when [id_phieu_giam_gia]<100 then '0'+CONVERT([varchar],[id_phieu_giam_gia]) else CONVERT([varchar],[id_phieu_giam_gia]) end")
    @Column(name = "ma_giam_gia", length = 34)
    private String maGiamGia;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_khuyen_mai", length = 50)
    private String tenKhuyenMai;

    @Size(max = 50)
    @Nationalized
    @Column(name = "loai_khuyen_mai", length = 50)
    private String loaiKhuyenMai;

    @Column(name = "gia_tri_khuyen_mai", precision = 10, scale = 2)
    private BigDecimal giaTriKhuyenMai;

    @Column(name = "gia_tri_don_hang_toi_thieu", precision = 10, scale = 2)
    private BigDecimal giaTriDonHangToiThieu;

    @Column(name = "gia_tri_khuyen_mai_toi_da", precision = 10, scale = 2)
    private BigDecimal giaTriKhuyenMaiToiDa;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Size(max = 100)
    @Nationalized
    @Column(name = "dieu_kien_ap_dung", length = 100)
    private String dieuKienApDung;

    @Size(max = 50)
    @Nationalized
    @Column(name = "hang_toi_thieu", length = 50)
    private String hangToiThieu;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "so_diem_can_de_doi", precision = 10, scale = 2)
    private BigDecimal soDiemCanDeDoi;

    @Column(name = "is_global")
    private Boolean isGlobal;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "trang_thai", length = 50)
    private TrangThaiPGG trangThai;

    @OneToMany(mappedBy = "idPhieuGiamGia",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<HoaDon> hoaDons = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idPhieuGiamGia",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<KhachHangGiamGia> khachHangGiamGias = new LinkedHashSet<>();

}