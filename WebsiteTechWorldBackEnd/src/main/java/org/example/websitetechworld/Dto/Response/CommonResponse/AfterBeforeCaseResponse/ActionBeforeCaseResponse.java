package org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActionBeforeCaseResponse {
    private Integer idXuLyBanHang;
    private String maHoaDon;
    private String tenKhachHang;
    private String sdt;
    private TrangThaiGiaoHang trangThaiDonHang;
    private LocalDateTime thoiGianYeuCau;
    private BigDecimal giaBan;

}
