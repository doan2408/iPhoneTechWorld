package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamHienThiAdminResponse {

    private Integer id;

    private String maSanPham;

    private String tenSanPham;

    private String tenLoai;

    private Integer soLuong;

    private BigDecimal giaBan;

    private String url;
}
