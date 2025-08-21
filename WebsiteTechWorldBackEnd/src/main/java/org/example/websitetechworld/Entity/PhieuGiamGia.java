package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPhatHanh;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Size(max = 50)
    @Nationalized
    @Column(name = "ma_giam_gia", length = 34)
    private String maGiamGia;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_giam_gia", length = 50)
    private String tenGiamGia;

    @Size(max = 50)
    @Nationalized
    @Column(name = "loai_giam_gia", length = 50)
    private String loaiGiamGia;

    @Column(name = "gia_tri_giam_gia", precision = 10, scale = 2)
    private BigDecimal giaTriGiamGia;

    @Column(name = "gia_tri_don_hang_toi_thieu", precision = 10, scale = 2)
    private BigDecimal giaTriDonHangToiThieu;

    @Column(name = "gia_tri_giam_gia_toi_da", precision = 10, scale = 2)
    private BigDecimal giaTriGiamGiaToiDa;

    @Column(name = "ngay_bat_dau")
    private LocalDateTime ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDateTime ngayKetThuc;

    @Size(max = 100)
    @Nationalized
    @Column(name = "dieu_kien_ap_dung", length = 100)
    private String dieuKienApDung;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "hang_toi_thieu", length = 50)
    private HangKhachHang hangToiThieu;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "so_diem_can_de_doi", precision = 10, scale = 2)
    private BigDecimal soDiemCanDeDoi;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "trang_thai_phieu_giam_gia", length = 50)
    private TrangThaiPGG trangThaiPhieuGiamGia;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "trang_thai_phat_hanh", length = 50)
    private TrangThaiPhatHanh trangThaiPhatHanh;

    @OneToMany(mappedBy = "idPhieuGiamGia",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<HoaDon> hoaDons = new LinkedHashSet<>();
}