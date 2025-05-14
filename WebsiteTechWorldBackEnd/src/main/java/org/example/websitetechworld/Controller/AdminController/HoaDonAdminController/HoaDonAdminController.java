package org.example.websitetechworld.Controller.AdminController.HoaDonAdminController;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.GetAllHoaDonAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.HoaDonAdminResponse;
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
}
