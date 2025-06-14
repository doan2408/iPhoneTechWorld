package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamHienThiAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamBanHangAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.SanPhamAdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class SanPhamAdminController {

    private final SanPhamAdminService adminService;


    @GetMapping
    public ResponseEntity<Page<SanPhamHienThiAdminResponse>> getSanPham(@RequestParam(value = "page",defaultValue = "0") int page) {
        int pageSize = 5;
        return ResponseEntity.ok(adminService.getAllSanPham(page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPhamAdminResponse> getSanPhamById(@PathVariable("id") int id) {
        return ResponseEntity.ok(adminService.detailSanPhamAdmin(id));
    }

    @PostMapping
    public ResponseEntity<SanPhamAdminResponse> createSanPham(@RequestBody SanPhamAdminRequest sanPhamAdminRequest) {
        SanPhamAdminResponse sanPham = adminService.createSanPhamAdmin(sanPhamAdminRequest);
        return ResponseEntity.ok(sanPham);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPhamAdminResponse> updateSanPham(@PathVariable Integer id, @RequestBody SanPhamAdminRequest sanPhamAdminRequest) {
        SanPhamAdminResponse sanPham = adminService.updateSanPhamAdmin(id, sanPhamAdminRequest);
        return ResponseEntity.ok(sanPham);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SanPhamAdminResponse> deleteSanPham(@PathVariable Integer id) {
        SanPhamAdminResponse sanPhamAdminResponse = adminService.deleteSanPhamAdmin(id);
        return ResponseEntity.ok(sanPhamAdminResponse);
    }

    // ham lay ten san pham
    @GetMapping("/ten-san-pham")
    public ResponseEntity<Page<SanPhamBanHangAdminResponse>> getTenSanPham( @RequestParam(value = "tenSanPham") String tenSanPham ,@RequestParam(value = "pageNo",defaultValue = "5") int pageNo, @RequestParam(value = "pageSize",defaultValue = "0") int pageSize) {
        return ResponseEntity.ok(adminService.getProductNames(tenSanPham,pageNo, pageSize));
    }

}
