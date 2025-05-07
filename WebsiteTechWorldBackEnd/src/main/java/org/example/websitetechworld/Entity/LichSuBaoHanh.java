package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiLichSuBaoHanh;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "lich_su_bao_hanh")
public class LichSuBaoHanh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_san_pham_bao_hanh")
    private BaoHanh idSanPhamBaoHanh;

    @Column(name = "ngay_tiep_nhan")
    private LocalDate ngayTiepNhan;

    @Column(name = "ngay_hoan_thanh")
    private LocalDate ngayHoanThanh;

    @Size(max = 255)
    @Nationalized
    @Column(name = "mo_ta_loi")
    private String moTaLoi;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "trang_thai", length = 50)
    private TrangThaiLichSuBaoHanh trangThai;

}