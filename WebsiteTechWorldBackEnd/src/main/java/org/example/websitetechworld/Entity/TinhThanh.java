package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "tinh_thanh")
public class TinhThanh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ten", length = 100)
    private String ten;

    @Column(name = "phi_ship", precision = 19, scale = 2)
    private BigDecimal phiShip;

}