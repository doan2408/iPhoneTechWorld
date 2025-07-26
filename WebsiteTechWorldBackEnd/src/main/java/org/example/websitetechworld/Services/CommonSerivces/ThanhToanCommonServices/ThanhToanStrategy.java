package org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices;

import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThanhToanAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ThanhToanAdminResponse;
import org.example.websitetechworld.Entity.HoaDon;
import org.springframework.stereotype.Component;

@Component
public interface ThanhToanStrategy {

    ThanhToanAdminResponse thanhToan(HoaDon hoaDon, ThanhToanAdminRequest request);
}
