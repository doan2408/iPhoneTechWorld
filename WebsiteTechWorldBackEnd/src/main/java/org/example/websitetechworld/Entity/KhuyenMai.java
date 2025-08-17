package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.KhuyenMai.DoiTuongApDung;
import org.example.websitetechworld.Enum.KhuyenMai.TrangThaiKhuyenMai;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "khuyen_mai")
public class KhuyenMai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Nationalized
    @Column(name = "ma_khuyen_mai", length = 50)
    private String maKhuyenMai;

    @Column(name = "ten_khuyen_mai", length = 255)
    private String tenKhuyenMai;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "phan_tram_giam")
    private Integer phanTramGiam;

    @Column(name = "ngay_bat_dau")
    private LocalDateTime ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDateTime ngayKetThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "doi_tuong_ap_dung", length = 50)
    private DoiTuongApDung doiTuongApDung;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private TrangThaiKhuyenMai trangThai;
}
