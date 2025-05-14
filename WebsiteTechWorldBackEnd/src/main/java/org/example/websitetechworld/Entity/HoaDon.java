package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.HoaDon.LoaiHoaDon;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hoa_don", nullable = false)
    private Integer id;

    @Size(max = 34)
    @Column(name = "ma_hoa_don", length = 34,insertable = false, updatable = false)
    private String maHoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang")
    private KhachHang idKhachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_phieu_giam_gia")
    private PhieuGiamGia idPhieuGiamGia;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_nguoi_nhan", length = 50)
    private String tenNguoiNhan;

    @Size(max = 100)
    @Nationalized
    @Column(name = "dia_chi", length = 100)
    private String diaChi;

    @Size(max = 10)
    @Nationalized
    @Column(name = "sdt", length = 10)
    private String sdt;

    @Column(name = "phi_ship", precision = 10, scale = 2)
    private BigDecimal phiShip;

    @Column(name = "tong_tien", precision = 10, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "so_tien_giam", precision = 10, scale = 2)
    private BigDecimal soTienGiam;

    @Column(name = "thanh_tien", precision = 10, scale = 2)
    private BigDecimal thanhTien;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "loai_hoa_don", length = 50)
    private LoaiHoaDon loaiHoaDon;

    @Column(name = "ngay_thanh_toan")
    private LocalDate ngayThanhToan;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "trang_thai_thanh_toan", length = 50)
    private TrangThaiThanhToan trangThaiThanhToan;

    @OneToMany(mappedBy = "idHoaDon",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ChiTietHoaDon> chiTietHoaDons = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idHoaDon",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ChiTietThanhToan> chiTietThanhToans = new LinkedHashSet<>();

    @OneToOne(mappedBy = "idHoaDon",cascade = CascadeType.ALL,orphanRemoval = true)
    private GiaoHang giaoHangs ;

    @OneToMany(mappedBy = "idHoaDon",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<LichSuHoaDon> lichSuHoaDons = new LinkedHashSet<>();

}