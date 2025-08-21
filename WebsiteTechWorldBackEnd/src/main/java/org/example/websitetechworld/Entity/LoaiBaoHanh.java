package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "loai_bao_hanh")
public class LoaiBaoHanh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loai_bao_hanh", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ten_loai_bao_hanh", length = 100)
    private String tenLoaiBaoHanh;

    @Column(name = "thoi_gian_thang")
    private Integer thoiGianThang;

    @Size(max = 255)
    @Nationalized
    @Column(name = "mo_ta")
    private String moTa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_model_san_pham")
    private ModelSanPham idModelSanPham;

    @OneToMany(mappedBy = "idLoaiBaoHanh",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<BaoHanh> baoHanhs = new LinkedHashSet<>();

}