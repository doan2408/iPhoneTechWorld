package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "camera_sau")
public class CameraSau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_camera_sau", nullable = false)
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

    @OneToMany(mappedBy = "cameraSau", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ModelCameraSau> modelCameraSaus = new ArrayList<>();

}