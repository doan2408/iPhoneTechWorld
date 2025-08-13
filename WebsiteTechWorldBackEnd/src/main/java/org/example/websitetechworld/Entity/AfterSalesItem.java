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
@Table(name = "after_sales_item")
public class AfterSalesItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "after_sales_item_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hoa_don_id", nullable = false)
    private HoaDon hoaDon;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "imei_da_ban_id", nullable = false)
    private ImeiDaBan imeiDaBan;

    @NotNull
    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "case_type", nullable = false, length = 20)
    private CaseType caseType;

    @NotNull
    @ColumnDefault("getdate()")
    @Column(name = "case_time", nullable = false)
    private LocalDateTime caseTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_id")
    private CaseReason reason;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "action_after_case", nullable = false, length = 20)
    private ActionAfterCase actionAfterCase;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "checked", nullable = false)
    private Boolean checked = false;

}