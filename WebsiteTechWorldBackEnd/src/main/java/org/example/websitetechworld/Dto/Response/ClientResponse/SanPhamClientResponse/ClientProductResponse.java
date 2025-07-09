package org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientProductResponse {
    private Integer id;
    private String hinhAnh;
    private String tenSanPham;
    private BigDecimal giaBan;
    private String tenLoai;
}
