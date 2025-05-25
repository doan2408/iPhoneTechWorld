package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.HoaDon.LoaiHinhThuc;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phuong_thuc_thanh_toan")
public class PhuongThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phuong_thuc_thanh_toan", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_phuong_thuc", length = 50)
    private String tenPhuongThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_hinh_thuc", length = 50)
    private LoaiHinhThuc loaiHinhThuc;

    @OneToMany(mappedBy = "idPhuongThucThanhToan",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<ChiTietThanhToan> chiTietThanhToans = new LinkedHashSet<>();

}