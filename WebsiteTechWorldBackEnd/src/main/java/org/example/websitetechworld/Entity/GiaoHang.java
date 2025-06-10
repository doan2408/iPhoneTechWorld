package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "giao_hang")
public class GiaoHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_giao_hang", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang")
    private KhachHang idKhachHang;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_hoa_don")
    private HoaDon idHoaDon;

    @Size(max = 34)
    @Column(name = "ma_giao_hang", length = 34, insertable = false, updatable = false)
    private String maGiaoHang;

    // Thêm thuộc tính maVanDon
    @Size(max = 34)
    @Column(name = "ma_van_don", length = 34, insertable = false, updatable = false)
    private String maVanDon;

    // Thêm thuộc tính tenNguoiNhan
    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_nguoi_nhan", length = 50)
    private String tenNguoiNhan;

    @Column(name = "ngay_dat_hang")
    private LocalDate ngayDatHang;

    @Column(name = "tong_gia_tri_don_hang", precision = 10, scale = 2)
    private BigDecimal tongGiaTriDonHang;

    @Size(max = 50)
    @Nationalized
    @Column(name = "dia_chi_giao_hang", length = 50)
    private String diaChiGiaoHang;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "trang_thai_don_hang", length = 50)
    private TrangThaiGiaoHang trangThaiDonHang;

    @OneToMany(mappedBy = "idGiaoHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTietGiaoHang> chiTietGiaoHangs = new LinkedHashSet<>();
}