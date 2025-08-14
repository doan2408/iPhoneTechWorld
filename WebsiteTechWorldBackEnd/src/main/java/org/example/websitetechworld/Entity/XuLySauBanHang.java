package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.ActionAfterCase;
import org.example.websitetechworld.Enum.CaseReason.CaseType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "xu_ly_sau_ban_hang")
public class XuLySauBanHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_xu_ly_sau_ban_hang", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_hoa_don", nullable = false)
    private HoaDon idHoaDon;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_imei_da_ban", nullable = false)
    private ImeiDaBan idImeiDaBan;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "loai_vu_viec", nullable = false, length = 20)
    private CaseType loaiVuViec;

    @ColumnDefault("getdate()")
    @Column(name = "thoi_gian_vu_viec", nullable = false)
    private LocalDateTime thoiGianVuViec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ly_do")
    private LyDoXuLy idLyDo;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "hanh_dong_sau_vu_viec", nullable = false, length = 20)
    private ActionAfterCase hanhDongSauVuViec;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "da_kiem_tra", nullable = false)
    private Boolean daKiemTra = false;

}