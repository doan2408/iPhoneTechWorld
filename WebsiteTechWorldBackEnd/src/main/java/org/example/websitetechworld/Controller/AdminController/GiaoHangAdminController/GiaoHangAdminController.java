package org.example.websitetechworld.Controller.AdminController.GiaoHangAdminController;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.GetAllGiaoHangResponseAdmin;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.ViewGiaoHangAdminResponse;
import org.example.websitetechworld.Entity.GiaoHang;
import org.example.websitetechworld.Services.AdminServices.GiaoHangAdminServces.GiaoHangAdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/giao-hang")
public class GiaoHangAdminController {
    private final GiaoHangAdminServices giaoHangAdminServices;

    private static final int PAGE_SIZE = 4;
    public GiaoHangAdminController(GiaoHangAdminServices giaoHangAdminServices) {
        this.giaoHangAdminServices = giaoHangAdminServices;
    }
    @GetMapping
    public List<GetAllGiaoHangResponseAdmin> getPageGiaoHang(@RequestParam(defaultValue = "0") int pageNo){
        return giaoHangAdminServices.getPageGiaoHang(pageNo,PAGE_SIZE);
    }
    @GetMapping("/{id}")
    public ViewGiaoHangAdminResponse findById(@PathVariable Integer id){
        return giaoHangAdminServices.findById(id);
    }
}
