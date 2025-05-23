package org.example.websitetechworld.Controller.AdminController.GiaoHangAdminController;

import org.example.websitetechworld.Dto.Request.AdminRequest.GiaoHangAdminRequest.AddGIaoHangAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.GetAllGiaoHangResponseAdmin;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.ViewGiaoHangAdminResponse;
import org.example.websitetechworld.Entity.GiaoHang;
import org.example.websitetechworld.Services.AdminServices.GiaoHangAdminServces.GiaoHangAdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.RequestContextFilter;

import java.util.List;

@RestController
@RequestMapping("/admin/giao-hang")
public class GiaoHangAdminController {
    private final GiaoHangAdminServices giaoHangAdminServices;

    private static final int PAGE_SIZE = 4;
    private final RequestContextFilter requestContextFilter;

    public GiaoHangAdminController(GiaoHangAdminServices giaoHangAdminServices, RequestContextFilter requestContextFilter) {
        this.giaoHangAdminServices = giaoHangAdminServices;
        this.requestContextFilter = requestContextFilter;
    }
    @GetMapping
    public List<GetAllGiaoHangResponseAdmin> getPageGiaoHang(@RequestParam(defaultValue = "0") int pageNo){
        return giaoHangAdminServices.getPageGiaoHang(pageNo,PAGE_SIZE);
    }
    @GetMapping("/{id}")
    public ViewGiaoHangAdminResponse findById(@PathVariable Integer id){
        return giaoHangAdminServices.findById(id);
    }

    @PostMapping()
    public ResponseEntity<?> addGiaoHang(@RequestBody AddGIaoHangAdminRequest addGIaoHangAdminRequest){
        giaoHangAdminServices.addGiaoHang(addGIaoHangAdminRequest);
        return ResponseEntity.ok("Da gui yeu cau giao hang");
    }
    @PutMapping("/packed")
    public ResponseEntity<?> changePacked(){
        giaoHangAdminServices.changePacked();
        return ResponseEntity.ok("Da xac nhan dong goi");
    }

    @PutMapping("/packed")
    public ResponseEntity<?> changeShipping(){
        giaoHangAdminServices.changeShipping();
        return ResponseEntity.ok("Da xac nhan dong goi");
    }
}
