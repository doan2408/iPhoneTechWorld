package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiBaoHanh;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "bao_hanh")
public class BaoHanh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bao_hanh", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang")
    private KhachHang idKhachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_san_pham_chi_tiet")
    private SanPhamChiTiet idSanPhamChiTiet;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_loai_bao_hanh")
    private LoaiBaoHanh idLoaiBaoHanh;


    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "trang_thai_bao_hanh", length = 50)
    private TrangThaiBaoHanh trangThaiBaoHanh;

    @OneToMany(mappedBy = "idSanPhamBaoHanh",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<LichSuBaoHanh> lichSuBaoHanhs = new LinkedHashSet<>();

}