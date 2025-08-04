package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiDanhGia;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "danh_gia_san_pham")
public class DanhGiaSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_danh_gia")
    private Integer idDanhGia;

    @Column(name = "so_sao", nullable = false)
    private Integer soSao;

    @Column(name = "noi_dung", length = 500)
    private String noiDung;

    @Column(name = "ngay_danh_gia")
    private LocalDateTime ngayDanhGia;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_danh_gia")
    private TrangThaiDanhGia trangThaiDanhGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang")
    private KhachHang idKhachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_san_pham_chi_tiet")
    private SanPhamChiTiet idSanPhamChiTiet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chi_tiet_hoa_don")
    private ChiTietHoaDon idChiTietHoaDon;

    @OneToMany(mappedBy = "danhGiaSanPham", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MediaDanhGia> mediaList;

    @OneToMany(mappedBy = "danhGiaSanPham", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhanHoiDanhGia> phanHoiList;

}
