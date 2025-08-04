package org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonAndChiTietHoaDonClientResponse {
    private Integer idHoaDon;
    private Integer idChiTietHoaDon;
    private Integer idSanPhamChiTiet;
}
