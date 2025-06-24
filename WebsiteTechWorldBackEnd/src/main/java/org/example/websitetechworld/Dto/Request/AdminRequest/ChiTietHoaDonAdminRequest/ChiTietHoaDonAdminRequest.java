package org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietHoaDonAdminRequest {
    private Integer idHoaDon;

    private Integer idSanPhamChiTiet;

    private Integer soLuong;

    private List<String> imeiIds;

}
