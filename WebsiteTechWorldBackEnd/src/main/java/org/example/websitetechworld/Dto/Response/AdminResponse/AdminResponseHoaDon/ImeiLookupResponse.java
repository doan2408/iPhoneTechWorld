package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImeiLookupResponse {
    private String soImei;
    private BigDecimal giaBan;
    private String tenSanPham;
    private Integer idSanPhamChiTiet;
}
