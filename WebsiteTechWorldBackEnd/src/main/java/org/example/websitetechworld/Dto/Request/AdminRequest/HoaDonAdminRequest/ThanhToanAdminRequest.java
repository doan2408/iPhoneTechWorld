package org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest;

import lombok.Data;
import org.example.websitetechworld.Enum.HoaDon.TenHinhThuc;

import java.math.BigDecimal;

@Data
public class ThanhToanAdminRequest {
    private TenHinhThuc hinhThucThanhToan;
    private BigDecimal soTienKhachDua;

}
