package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "hang_thanh_vien")
public class HangThanhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hang_thanh_vien", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "ten_hang", length = 50)
    private HangKhachHang tenHang;

    @NotNull
    @Column(name = "diem_tu", nullable = false)
    private Integer diemTu;

    @Column(name = "diem_den")
    private Integer diemDen;

    @OneToMany(mappedBy = "hangThanhVien")
    private Set<KhachHang> khachHangs = new LinkedHashSet<>();

}