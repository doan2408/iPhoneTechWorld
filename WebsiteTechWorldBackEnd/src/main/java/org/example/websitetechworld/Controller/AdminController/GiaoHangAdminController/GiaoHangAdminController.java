package org.example.websitetechworld.Controller.AdminController.GiaoHangAdminController;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.GetAllGiaoHangResponseAdmin;
import org.example.websitetechworld.Entity.GiaoHang;
import org.example.websitetechworld.Services.AdminServices.GiaoHangAdminServces.GiaoHangAdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/giao-hang")
public class GiaoHangAdminController {
    private final GiaoHangAdminServices giaoHangAdminServices;

    public GiaoHangAdminController(GiaoHangAdminServices giaoHangAdminServices) {
        this.giaoHangAdminServices = giaoHangAdminServices;
    }
    @GetMapping
    public List<GetAllGiaoHangResponseAdmin> getPageGiaoHang(@RequestParam(defaultValue = "0") int pageNo){
        int pageSize = 4;
        return giaoHangAdminServices.getPageGiaoHang(pageNo,pageSize);
    }
}
