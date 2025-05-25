package org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest;

import lombok.Data;
import org.example.websitetechworld.Enum.HoaDon.LoaiHinhThuc;

import java.math.BigDecimal;

@Data
public class ThanhToanAdminRequest {
    private LoaiHinhThuc hinhThucThanhToan;
    private BigDecimal soTienKhachDua;

}
