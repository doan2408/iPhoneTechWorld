package org.example.websitetechworld.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phan_hoi_danh_gia")
public class PhanHoiDanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phan_hoi")
    private Integer idPhanHoi;

    @Column(name = "id_danh_gia", nullable = false)
    private Integer idDanhGia;

    @Column(name = "noi_dung", length = 500)
    private String noiDung;

    @Column(name = "ngay_phan_hoi")
    private LocalDateTime ngayPhanHoi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_danh_gia", insertable = false, updatable = false)
    private DanhGiaSanPham danhGiaSanPham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nhan_vien", nullable = false)
    private NhanVien idNhanVien;
}
