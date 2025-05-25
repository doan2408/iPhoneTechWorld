package org.example.websitetechworld.Dto.Response.AdminResponse.ThongKeAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThongKeDonhangAdminResponse {

    private Integer tongSoDonHang;

    private Integer donHangChuaXuLy;

    private Integer donHangDaThanhToan;

    private Integer donHangBiHuy;

    private Integer donHangDaHoanTien;

    private Integer donHangDaHoanTat;
}