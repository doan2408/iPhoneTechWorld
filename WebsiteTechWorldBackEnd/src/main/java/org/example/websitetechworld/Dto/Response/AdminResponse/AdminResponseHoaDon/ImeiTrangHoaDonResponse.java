package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImeiTrangHoaDonResponse {
    private Integer idImei;
    private String soImei;
    private TrangThaiImei trangThai;
    private BigDecimal giaBan;
    private Integer idHoaDonChiTiet;
    private Integer idSanPhamChiTiet;
    private String tenSanPham;
    private String dungLuongRom;
    private String tenMau;
}
