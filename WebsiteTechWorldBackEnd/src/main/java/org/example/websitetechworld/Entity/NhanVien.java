package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.NhanVien.NhanVienChucVu;
import org.example.websitetechworld.Enum.NhanVien.NhanVienTrangThai;
import org.example.websitetechworld.Repository.JointAccount;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "nhan_vien")
public class NhanVien implements JointAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nhan_vien", nullable = false)
    private Integer id;

    @Size(max = 34)
    @Column(name = "ma_nhan_vien", length = 34,insertable = false, updatable = false)
    private String maNhanVien;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_nhan_vien", length = 50)
    private String tenNhanVien;

    @Size(max = 50)
    @Column(name = "tai_khoan", length = 50)
    private String taiKhoan;

    @Size(max = 255)
    @Column(name = "mat_khau", length = 255)
    private String matKhau;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 10)
    @Column(name = "sdt", length = 10)
    private String sdt;

    @Size(max = 100)
    @Nationalized
    @Column(name = "dia_chi", length = 100)
    private String diaChi;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "trang_thai", length = 50)
    private NhanVienTrangThai trangThai;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "chuc_vu", length = 50)
    private NhanVienChucVu chucVu;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @OneToMany(mappedBy = "idNhanVien", cascade = {CascadeType.PERSIST , CascadeType.MERGE})
    private Set<LichSuHoaDon> lichSuHoaDons = new LinkedHashSet<>();


    @Override
    public String tai_khoan() {
        return this.taiKhoan;
    }

    @Override
    public String mat_khau() {
        return this.matKhau;
    }

    @Override
    public String getRole() {
        return "ROLE_" +chucVu.name();
    }

    @Override
    public String getFullName() {
        return this.tenNhanVien;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getSdt() {
        return this.sdt;
    }

    @Override
    public String getTrangThai() {
        return String.valueOf(this.trangThai);
    }

}