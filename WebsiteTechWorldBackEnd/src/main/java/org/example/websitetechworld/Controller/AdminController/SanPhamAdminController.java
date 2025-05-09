package org.example.websitetechworld.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class SanPhamAdminController {

    private final SanPhamAdminService adminService;

    @GetMapping("/product")
    public ResponseEntity<?> getSanPham() {
        return ResponseEntity.ok(adminService.getAllSanPham());
    }
}
