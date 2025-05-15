package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamChiTietAdminRepuest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.SanPhamChiTietAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/sanPhamChiTiet")
public class SanPhamChiTietAdminController {

    private final SanPhamChiTietAdminService sanPhamChiTietAdminService;

    @GetMapping("/{id}")
    public ResponseEntity<SanPhamChiTietResponse> getSanPhamChiTiet(@PathVariable("id") Integer id) {
        SanPhamChiTiet entity = sanPhamChiTietAdminService.getChiTietById(id);

        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        SanPhamChiTietResponse response = sanPhamChiTietAdminService.getSanPhamChiTiet(entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<SanPhamChiTietResponse> createSPCT(@RequestBody SanPhamChiTietAdminRepuest sanPhamChiTietAdminRepuest) {
        SanPhamChiTietResponse response = sanPhamChiTietAdminService.createSanPhamChiTiet(sanPhamChiTietAdminRepuest);

        return ResponseEntity.ok(response);
    }
}
