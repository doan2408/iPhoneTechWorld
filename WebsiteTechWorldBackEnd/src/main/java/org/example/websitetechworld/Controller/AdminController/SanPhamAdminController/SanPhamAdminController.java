package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.AdminProductResponse;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.SanPhamAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class SanPhamAdminController {

    private final SanPhamAdminService adminService;

    @GetMapping
    public ResponseEntity<List<AdminProductResponse>> getSanPham() {
        return ResponseEntity.ok(adminService.getAllSanPham());
    }

    @PostMapping("/create")
    public ResponseEntity<SanPham> createSanPham(@RequestBody SanPhamAdminRequest sanPhamAdminRequest) {
        SanPham sanPham = adminService.createSanPhamAdmin(sanPhamAdminRequest);
        return ResponseEntity.ok(sanPham);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPham> createSanPham(@PathVariable Integer id, @RequestBody SanPhamAdminRequest sanPhamAdminRequest) {
        SanPham sanPham = adminService.updateSanPhamAdmin(id,sanPhamAdminRequest);
        return ResponseEntity.ok(sanPham);
    }


}
