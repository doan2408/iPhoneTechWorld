package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "san_pham_chi_tiet")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mau")
    private MauSac idMau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ram")
    private Ram idRam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rom")
    private Rom idRom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_man_hinh")
    private ManHinh idManHinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_he_dieu_hanh")
    private HeDieuHanh idHeDieuHanh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pin")
    private Pin idPin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cpu")
    private Cpu idCpu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_camera_truoc")
    private CameraTruoc idCameraTruoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_camera_sau")
    private CameraSau idCameraSau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_xuat_xu")
    private XuatXu idXuatXu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_loai")
    private Loai idLoai;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "gia_ban", precision = 10, scale = 2)
    private BigDecimal giaBan;

    @OneToMany(mappedBy = "idSanPhamChiTiet",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<BaoHanh> baoHanhs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSanPhamChiTiet",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<ChiTietGiaoHang> chiTietGiaoHangs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSanPhamChiTiet",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<ChiTietHoaDon> chiTietHoaDons = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSanPhamChiTiet",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<GioHangChiTiet> gioHangChiTiets = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSanPhamChiTiet",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<HinhAnh> hinhAnhs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSanPhamChiTiet",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Imei> imeis = new LinkedHashSet<>();



}