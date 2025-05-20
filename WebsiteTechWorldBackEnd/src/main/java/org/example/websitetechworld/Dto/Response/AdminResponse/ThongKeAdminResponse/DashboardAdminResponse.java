package org.example.websitetechworld.Dto.Response.AdminResponse.ThongKeAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardAdminResponse {

    private BigDecimal doanhThuThang;

    private Integer tongSoDonhang;

    private Integer tongSoSanPham;

    private Integer tongSoKhachHang;
}
