package org.example.websitetechworld.Controller.AdminController.GiaoHangAdminController;

import org.example.websitetechworld.Dto.Request.AdminRequest.GiaoHangAdminRequest.AddGIaoHangAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.GetAllGiaoHangResponseAdmin;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.ViewGiaoHangAdminResponse;
import org.example.websitetechworld.Entity.GiaoHang;
import org.example.websitetechworld.Services.AdminServices.GiaoHangAdminServces.GiaoHangAdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public Page<GetAllGiaoHangResponseAdmin> getPageGiaoHang(@RequestParam(defaultValue = "0") int pageNo,
                                                             @RequestParam(defaultValue = "10", value = "pageSize") int pageSize){
        return giaoHangAdminServices.getPageGiaoHang(pageNo,pageSize);
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
    @PutMapping("/{idGiaoHang}/packed")
    public ResponseEntity<?> changePacked(@PathVariable Integer idGiaoHang){
        giaoHangAdminServices.changePacked(idGiaoHang);
        return ResponseEntity.ok("Da xac nhan dong goi");
    }

    @PutMapping("/{idGiaoHang}/shipping")
    public ResponseEntity<?> changeShipping(@PathVariable Integer idGiaoHang){
        giaoHangAdminServices.changeShipping(idGiaoHang);
        return ResponseEntity.ok("Da chuyen qua don vi van chuyen");
    }
}
