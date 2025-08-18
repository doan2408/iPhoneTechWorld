package org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.ActionAfterCase;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class XuLyChiTietResponse {
    private Integer idXlbh;
    private Integer idHoaDon;
    private String maHoaDon;
    private String tenKhachHang;
    private String soImei;
    private ActionAfterCase trangThaiDon;
    private String tenSanPham;
    private String dungLuong;
    private String mau;
    private BigDecimal soTienHoan;
}