package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.KhachHang.LoaiDiem;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "lich_su_diem")
public class LichSuDiem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lich_su_diem", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vi_diem")
    private ViDiem viDiem;

    @Column(name = "so_diem", precision = 10, scale = 2)
    private BigDecimal soDiem;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_diem", length = 10)
    private LoaiDiem loaiDiem;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ghi_chu", length = 50)
    private String ghiChu;

    @Column(name = "thoi_gian")
    private OffsetDateTime thoiGian;

    @Column(name = "han_su_dung")
    private OffsetDateTime hanSuDung;

    @OneToMany(mappedBy = "lichSuDiem")
    private Set<ChiTietLichSuDiem> chiTietLichSuDiems = new LinkedHashSet<>();

}