package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "man_hinh")
public class ManHinh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_man_hinh", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_man_hinh", length = 50)
    private String tenManHinh;

    @Size(max = 50)
    @Nationalized
    @Column(name = "kich_thuoc", length = 50)
    private String kichThuoc;

    @Size(max = 50)
    @Nationalized
    @Column(name = "loai_man_hinh", length = 50)
    private String loaiManHinh;

    @Size(max = 50)
    @Nationalized
    @Column(name = "do_phan_giai", length = 50)
    private String doPhanGiai;

    @Size(max = 50)
    @Nationalized
    @Column(name = "tan_so_quet", length = 50)
    private String tanSoQuet;

    @Size(max = 50)
    @Nationalized
    @Column(name = "do_sang", length = 50)
    private String doSang;

    @Size(max = 50)
    @Nationalized
    @Column(name = "chat_lieu_kinh", length = 50)
    private String chatLieuKinh;

}