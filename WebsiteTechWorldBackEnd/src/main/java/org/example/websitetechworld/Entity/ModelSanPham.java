package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "model_san_pham")
public class ModelSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_model_san_pham")
    private Integer idModelSanPham;

    @Column(name = "ma_model_san_pham", insertable = false, updatable = false)
    private String maModelSanPham;

    @Column(name = "ten_model")
    private String tenModel;

    @Temporal(TemporalType.DATE)
    @Column(name = "nam_ra_mat")
    private LocalDate namRaMat;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "trang_thai")
    private String trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ram")
    private Ram idRam;

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

}
