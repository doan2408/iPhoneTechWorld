package org.example.websitetechworld.Controller.AdminController.GiaoHangAdminController;

import org.example.websitetechworld.Dto.Request.AdminRequest.GiaoHangAdminRequest.AddGIaoHangAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.GiaoHangAdminRequest.CapNhatTrangThaiDTO;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.GetAllGiaoHangResponseAdmin;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.ViewGiaoHangAdminResponse;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;
import org.example.websitetechworld.Services.AdminServices.GiaoHangAdminServces.GiaoHangAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon.HoaDonAdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.RequestContextFilter;

@RestController
@RequestMapping("/admin/giao-hang")
public class GiaoHangAdminController {
    private final HoaDonAdminService hoaDonAdminService;
    private final GiaoHangAdminServices giaoHangAdminServices;

    private final RequestContextFilter requestContextFilter;

    public GiaoHangAdminController(HoaDonAdminService hoaDonAdminService, GiaoHangAdminServices giaoHangAdminServices, RequestContextFilter requestContextFilter) {
        this.hoaDonAdminService = hoaDonAdminService;
        this.giaoHangAdminServices = giaoHangAdminServices;
        this.requestContextFilter = requestContextFilter;
    }

    @PutMapping("/{idHoaDon}/status")
        public ResponseEntity<?> updateStatus(@PathVariable Integer idHoaDon,
                                          @RequestBody CapNhatTrangThaiDTO request) {
        TrangThaiGiaoHang trangThai;
        try {
            trangThai = TrangThaiGiaoHang.valueOf(request.getTrangThaiDonHang().toUpperCase());
        } catch (IllegalArgumentException e) {
            trangThai = TrangThaiGiaoHang.fromDisplayName(request.getTrangThaiDonHang());
        }
        giaoHangAdminServices.updateStatus(idHoaDon, trangThai);
        return ResponseEntity.ok("Đã cập nhật trạng thái: " + trangThai.getDisplayName());
    }
}

