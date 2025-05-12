package org.example.websitetechworld.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.AdminProductRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminProductResponse;
import org.example.websitetechworld.Repository.SanPhamRepository;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class SanPhamAdminController {

    private final SanPhamAdminService adminService;

    @GetMapping("")
    public ResponseEntity<List<AdminProductResponse>> getSanPham() {
        return ResponseEntity.ok(adminService.getAllSanPham());
    }

}
