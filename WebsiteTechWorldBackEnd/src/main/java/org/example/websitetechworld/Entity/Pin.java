package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "pin")
public class Pin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pin", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "phien_ban", length = 50)
    private String phienBan;

    @Size(max = 50)
    @Nationalized
    @Column(name = "cong_suat_sac", length = 50)
    private String congSuatSac;

    @Size(max = 50)
    @Nationalized
    @Column(name = "thoi_gian_su_dung", length = 50)
    private String thoiGianSuDung;

    @Size(max = 50)
    @Nationalized
    @Column(name = "so_lan_sac_toi_da", length = 50)
    private String soLanSacToiDa;

}