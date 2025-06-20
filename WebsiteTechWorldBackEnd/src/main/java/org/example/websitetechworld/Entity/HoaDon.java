package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;
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
    @Column(name = "ma_hoa_don", length = 34, insertable = false, updatable = false)
    private String maHoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang")
    private KhachHang idKhachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_phieu_giam_gia")
    private PhieuGiamGia idPhieuGiamGia;

    // Đổi từ tenNguoiNhan thành tenNguoiMua
    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_nguoi_mua", length = 50)
    private String tenNguoiMua;

    @Size(max = 10)
    @Nationalized
    @Column(name = "sdt_nguoi_mua", length = 10)
    private String sdtNguoiMua;

    // Xóa thuộc tính diaChi

    @Column(name = "phi_ship", precision = 10, scale = 2)
    private BigDecimal phiShip;

    @Column(name = "tong_tien", precision = 10, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "so_tien_giam", precision = 10, scale = 2)
    private BigDecimal soTienGiam;

    @Column(name = "thanh_tien", precision = 10, scale = 2)
    private BigDecimal thanhTien;

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

    @OneToMany(mappedBy = "idHoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTietHoaDon> chiTietHoaDons = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idHoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTietThanhToan> chiTietThanhToans = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idHoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LichSuHoaDon> lichSuHoaDons = new LinkedHashSet<>();

    @Size(max = 20)
    @Nationalized
    @Column(name = "ma_van_don", length = 20)
    private String maVanDon;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_nguoi_nhan", length = 50)
    private String tenNguoiNhan;

    @Size(max = 10)
    @Nationalized
    @Column(name = "sdt_nguoi_nhan", length = 10)
    private String sdtNguoiNhan;

    @Size(max = 100)
    @Nationalized
    @Column(name = "dia_chi_giao_hang", length = 100)
    private String diaChiGiaoHang;

    @Column(name = "ngay_dat_hang")
    private LocalDate ngayDatHang;

    @Column(name = "ngay_tao_hoa_don")
    private LocalDate ngayTaoHoaDon;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "trang_thai_don_hang", length = 50)
    private TrangThaiGiaoHang trangThaiDonHang;

}