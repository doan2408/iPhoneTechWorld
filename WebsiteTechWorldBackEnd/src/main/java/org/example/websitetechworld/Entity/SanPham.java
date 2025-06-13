package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPham;
import org.hibernate.annotations.Nationalized;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "san_pham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_san_pham", nullable = false)
    private Integer id;

    @Size(max = 34)
    @Column(name = "ma_san_pham", length = 34, insertable = false, updatable = false)
    private String maSanPham;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_san_pham", length = 50)
    private String tenSanPham;

    @Size(max = 50)
    @Nationalized
    @Column(name = "thuong_hieu", length = 50)
    private String thuongHieu;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private TrangThaiSanPham trangThaiSanPham;

    // Xóa thuộc tính soLuongTonKho

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nha_cung_cap")
    private NhaCungCap idNhaCungCap;

    @OneToMany(mappedBy = "idSanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SanPhamChiTiet> sanPhamChiTiets = new LinkedHashSet<>();
}