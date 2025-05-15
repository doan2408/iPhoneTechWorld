package org.example.websitetechworld.Controller.AdminController.HoaDonAdminController;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.*;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDonAdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hoa-don")
public class HoaDonAdminController {
    private final HoaDonAdminService hoaDonAdminService;

    public HoaDonAdminController(HoaDonAdminService hoaDonAdminService) {
        this.hoaDonAdminService = hoaDonAdminService;
    }

    @GetMapping
    public List<GetAllHoaDonAdminResponse> getAll(@RequestParam(defaultValue = "0",value = "pageNo") int pageNo){
        int pageSize = 4;
        return hoaDonAdminService.getPageHoaDon(pageNo,pageSize);
    }
    @GetMapping("/{id}")
    public HoaDonAdminResponse findById(@PathVariable("id") int id){
        return hoaDonAdminService.findById(id);
    }

    @GetMapping("/{id}/lich-su")
    public List<LichSuHoaDonAdminResponse> getPageHoaDon(@PathVariable Integer id, @RequestParam(defaultValue = "0") int pageNo){
        int pageSize = 4;
        return hoaDonAdminService.getPageLichSuHoaDon(id,pageNo,pageSize);
    }
    @GetMapping("/{id}/chi-tiet-thanh-toan")
    public List<ChiTietThanhToanAdminResponse> getPageChiTietThanhToan(@PathVariable Integer id, @RequestParam(defaultValue = "0") int pageNo){
        int pageSize = 4;
        return hoaDonAdminService.getPageChiTietThanhToan(id,pageNo,pageSize);
    }
    @GetMapping("/{id}/xem-giao-hang")
    public List<GiaoHangAdminResponse> getPageGiaoHang(@PathVariable Integer id, @RequestParam(defaultValue = "0") int pageNo){
        int pageSize = 4;
        return hoaDonAdminService.getPageGiaoHang(id,pageNo,pageSize);
    }
}
