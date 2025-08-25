package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.ActionAfterCase;
import org.example.websitetechworld.Enum.CaseReason.CaseType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class XuLySauBanHangResponse {
    private Integer id;
    private Integer idHoaDon;
    private Integer idImeiDaBan;
    private String soImei;
    private CaseType caseType;
    private String loaiVuViec;
    private Integer idLyDoXuLy;
    private String lyDoXuLy;
    private ActionAfterCase actionAfterCase;
    private String hanhDongSauVuViec;
    private Boolean daKiemTra;
    private LocalDateTime thoiGianXuLy;
    private LocalDateTime thoiGianYeuCau;
    private String urlHinh;
    private String urlVideo;
}
