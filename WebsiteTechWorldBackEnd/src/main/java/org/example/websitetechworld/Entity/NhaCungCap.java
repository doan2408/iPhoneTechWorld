package org.example.websitetechworld.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "nha_cung_cap")
public class NhaCungCap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nha_cung_cap", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ten_nha_cung_cap", length = 50)
    private String tenNhaCungCap;

    @Size(max = 50)
    @Nationalized
    @Column(name = "dia_chi", length = 50)
    private String diaChi;

    @Size(max = 10)
    @Column(name = "sdt", length = 10)
    private String sdt;

    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @OneToMany(mappedBy = "nhaCungCap", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NhaCungCapSp> nhaCungCapSps = new LinkedHashSet<>();

}