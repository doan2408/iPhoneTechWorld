package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "camera_truoc")
public class CameraTruoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_camera_truoc", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Nationalized
    @Column(name = "loai_camera", length = 100)
    private String loaiCamera;

    @Size(max = 50)
    @Nationalized
    @Column(name = "do_phan_giai", length = 50)
    private String doPhanGiai;

    @Size(max = 50)
    @Nationalized
    @Column(name = "khau_do", length = 50)
    private String khauDo;

    @Size(max = 50)
    @Nationalized
    @Column(name = "loai_zoom", length = 50)
    private String loaiZoom;

    @Size(max = 50)
    @Nationalized
    @Column(name = "che_do_chup", length = 50)
    private String cheDoChup;

}