package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "imei")
public class Imei {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imei", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_san_pham_chi_tiet")
    private SanPhamChiTiet idSanPhamChiTiet;

    @Size(max = 70)
    @Column(name = "so_imei", length = 70)
    private String soImei;

    @Size(max = 70)
    @Column(name = "so_imei_2", length = 70)
    private String soImei2;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "trang_thai_imei", length = 50)
    private TrangThaiImei trangThaiImei;

    @Size(max = 50)
    @Nationalized
    @Column(name = "nha_mang", length = 50)
    private String nhaMang;



}