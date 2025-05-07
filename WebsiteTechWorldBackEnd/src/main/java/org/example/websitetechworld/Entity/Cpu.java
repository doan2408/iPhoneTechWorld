package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "cpu")
public class Cpu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cpu", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "hang_san_xuat", length = 50)
    private String hangSanXuat;

    @Size(max = 50)
    @Nationalized
    @Column(name = "so_nhan", length = 50)
    private String soNhan;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Size(max = 50)
    @Nationalized
    @Column(name = "xung_nhip", length = 50)
    private String xungNhip;

    @Size(max = 50)
    @Nationalized
    @Column(name = "cong_nghe_san_xuat", length = 50)
    private String congNgheSanXuat;

    @Size(max = 50)
    @Nationalized
    @Column(name = "bo_nho_dem", length = 50)
    private String boNhoDem;

    @Size(max = 50)
    @Nationalized
    @Column(name = "tieu_thu_dien_nang", length = 50)
    private String tieuThuDienNang;

    @Column(name = "nam_ra_mat")
    private LocalDate namRaMat;

}