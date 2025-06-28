package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "mau_sac")
public class MauSac {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mau_sac", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_mau", length = 50)
    private String tenMau;

    @Size(max = 10)
    @Nationalized
    @Column(name = "hex_color", length = 10)
    private String maMau;
}