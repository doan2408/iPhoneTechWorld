package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ram")
public class Ram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ram", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "dung_luong_ram", length = 50)
    private String dungLuong;

    @Size(max = 50)
    @Nationalized
    @Column(name = "loai_ram", length = 50)
    private String loai;

    @Size(max = 50)
    @Nationalized
    @Column(name = "toc_do_doc_ghi", length = 50)
    private String tocDoDocGhi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "nha_san_xuat", length = 50)
    private String nhaSanXuat;

    @Column(name = "nam_ra_mat")
    private LocalDate namRaMat;

}