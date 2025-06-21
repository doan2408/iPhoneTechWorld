package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "imei_da_ban")
public class ImeiDaBan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imei_da_ban", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_chi_tiet_hoa_don")
    private ChiTietHoaDon idHoaDonChiTiet;

    @Size(max = 70)
    @Column(name = "so_imei", length = 70)
    private String soImei;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "trang_thai", length = 50)
    private TrangThaiImei trangThai;

}