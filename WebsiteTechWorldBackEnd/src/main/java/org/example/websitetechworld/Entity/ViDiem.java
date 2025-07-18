package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "vi_diem")
public class ViDiem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vi_diem", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_khach_hang")
    private KhachHang khachHang;

    @Column(name = "diem_kha_dung", precision = 10, scale = 2)
    private BigDecimal diemKhaDung;

    @OneToMany(mappedBy = "viDiem")
    private Set<LichSuDiem> lichSuDiems = new LinkedHashSet<>();

}