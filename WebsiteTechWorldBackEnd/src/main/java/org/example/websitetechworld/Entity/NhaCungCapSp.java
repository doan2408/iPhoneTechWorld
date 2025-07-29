package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NhaCungCapSp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nha_cung_cap_sp")
    private Integer idNhaCungCapSp;

    @ManyToOne
    @JoinColumn(name = "id_nha_cung_cap")
    private NhaCungCap nhaCungCap;

    @ManyToOne
    @JoinColumn(name = "id_san_pham")
    private SanPham sanPham;
}
