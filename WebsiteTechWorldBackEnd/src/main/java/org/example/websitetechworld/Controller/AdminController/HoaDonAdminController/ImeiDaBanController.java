package org.example.websitetechworld.Controller.AdminController.HoaDonAdminController;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ImeiDaBangAdminResponse;
import org.example.websitetechworld.Entity.Imei;
import org.example.websitetechworld.Entity.ImeiDaBan;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ImeiDaBan.ImeiDaBanAdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/imei-da-ban")
public class ImeiDaBanController {
    private final ImeiDaBanAdminServices imeiDaBanAdminServices;

    public ImeiDaBanController(ImeiDaBanAdminServices imeiDaBanAdminServices) {
        this.imeiDaBanAdminServices = imeiDaBanAdminServices;
    }

    @GetMapping()
    public List<ImeiDaBangAdminResponse> getAll(@RequestParam(value = "idCthd") Integer idCthd){
        return imeiDaBanAdminServices.getImeiDaBanByIdCthd(idCthd);
    }

    @GetMapping("/{idKhachHang}")
    public ResponseEntity<List<ImeiDaBangAdminResponse>> getImeiDaBanByKhachHang(@PathVariable Integer idKhachHang){
        return ResponseEntity.ok(imeiDaBanAdminServices.imeiDaBanListByKhachHang(idKhachHang));
    }
}
