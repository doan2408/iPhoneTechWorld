package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "he_dieu_hanh")
public class HeDieuHanh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_he_dieu_hanh", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "phien_ban", length = 50)
    private String phienBan;

    @Size(max = 50)
    @Nationalized
    @Column(name = "nha_phat_trien", length = 50)
    private String nhaPhatTrien;

    @Size(max = 50)
    @Nationalized
    @Column(name = "giao_dien_nguoi_dung", length = 50)
    private String giaoDienNguoiDung;

}