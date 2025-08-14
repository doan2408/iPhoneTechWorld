package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ly_do_xu_ly")
public class LyDoXuLy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ly_do", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ten_ly_do", nullable = false)
    private String tenLyDo;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @Column(name = "loai_vu_viec", nullable = false, length = 20)
    private String loaiVuViec;

    @OneToMany(mappedBy = "idLyDo")
    private Set<XuLySauBanHang> xuLySauBanHangs = new LinkedHashSet<>();

}