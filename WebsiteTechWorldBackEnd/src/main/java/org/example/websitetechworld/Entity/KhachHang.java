package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;
import org.example.websitetechworld.Repository.JointAccount;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "khach_hang")
public class KhachHang implements JointAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_khach_hang", nullable = false)
    private Integer id;

    @Size(max = 34)
    @Column(name = "ma_khach_hang", length = 34, insertable = false, updatable = false)
    private String maKhachHang;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_khach_hang", length = 50)
    private String tenKhachHang;

    @Size(max = 10)
    @Column(name = "sdt", length = 10)
    private String sdt;

    @Size(max = 50)
    @Nationalized
    @Column(name = "tai_khoan", length = 50)
    private String taiKhoan;

    @Size(max = 255)
    @Column(name = "mat_khau", length = 255)
    private String matKhau;

    @Size(max = 100)
    @Nationalized
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @Size(max = 200)
    @Nationalized
    @Column(name = "anh", length = 200)
    private String anh;

    @Column(name = "tong_diem", precision = 10, scale = 2)
    private BigDecimal tongDiem;

    @Column(name = "so_diem_hien_tai", precision = 10, scale = 2)
    private BigDecimal soDiemHienTai;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "hang_khach_hang", length = 50)
    private HangKhachHang hangKhachHang;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "trang_thai", length = 50)
    private TrangThaiKhachHang trangThai;

    @OneToMany(mappedBy = "idKhachHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DiaChi> diaChis = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idKhachHang", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<HoaDon> hoaDons = new LinkedHashSet<>();


    @OneToMany(mappedBy = "idKhachHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<KhachHangGiamGia> khachHangGiamGias = new LinkedHashSet<>();

    @OneToOne(mappedBy = "idKhachHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private GioHang gioHang;


    @Override
    public String getRole() {
        return "ROLE_KHACH_HANG";
    }

    @Override
    public String tai_khoan() {
        return this.taiKhoan;
    }

    @Override
    public String mat_khau() {
        return this.matKhau;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public String getFullName() {
        return this.tenKhachHang;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getSdt() {
        return this.sdt;
    }

}