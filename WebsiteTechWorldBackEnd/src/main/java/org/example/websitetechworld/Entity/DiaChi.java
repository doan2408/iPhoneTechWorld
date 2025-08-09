package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "dia_chi")
public class DiaChi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dia_chi", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_khach_hang")
    private KhachHang idKhachHang;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_nguoi_nhan", length = 50)
    private String tenNguoiNhan;

    @Size(max = 10)
    @Column(name = "sdt_nguoi_nhan", length = 10)
    private String sdtNguoiNhan;

    @Size(max = 100)
    @Nationalized
    @Column(name = "email_nguoi_nhan", length = 100)
    private String emailNguoiNhan;

    @Size(max = 50)
    @Nationalized
    @Column(name = "so_nha", length = 50)
    private String soNha;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_duong", length = 50)
    private String tenDuong;

    @Size(max = 50)
    @Nationalized
    @Column(name = "xa_phuong", length = 50)
    private String xaPhuong;

    @Size(max = 50)
    @Nationalized
    @Column(name = "tinh_thanh_pho", length = 50)
    private String tinhThanhPho;

    @Column(name = "dia_chi_chinh")
    private Boolean diaChiChinh;

    public String getDiaChiDayDu() {
        return soNha + ", " + tenDuong + ", " + xaPhuong + ", " + tinhThanhPho;
    }
}