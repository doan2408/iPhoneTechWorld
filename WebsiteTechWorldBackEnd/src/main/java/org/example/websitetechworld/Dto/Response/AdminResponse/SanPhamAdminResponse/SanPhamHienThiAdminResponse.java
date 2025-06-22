package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPham;

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

    @Enumerated(EnumType.STRING)
    private TrangThaiSanPham trangThaiSanPham;

    private BigDecimal giaBan;

    private String url;

}
