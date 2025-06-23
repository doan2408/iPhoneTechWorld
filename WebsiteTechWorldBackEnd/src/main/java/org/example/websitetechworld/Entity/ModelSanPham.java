package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
@Table(name = "model_san_pham")
public class ModelSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id_model_san_pham;
    private String ma_model_san_pham;
    private String ten_model;
    private Date nam_ra_mat;
    private String mo_ta;
    private String trang_thai;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_san_pham")
    private SanPham idSanPham;


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
