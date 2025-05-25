package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ManHinhAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.MauSacAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ManHinhAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.MauSacAdminResponse;
import org.example.websitetechworld.Entity.ManHinh;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.ManHinhAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/manHinh")
public class ManHinhAdminController {
    private final ManHinhAdminService manHinhAdminService;

    @GetMapping
    public ResponseEntity<Page<ManHinhAdminResponse>> getAllManHinh(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<ManHinhAdminResponse> manHinhs = manHinhAdminService.getAllManHinh(pageable);
        return ResponseEntity.ok(manHinhs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManHinhAdminResponse> detailManHinh(@PathVariable Integer id) {
        ManHinhAdminResponse response = manHinhAdminService.detailManHinh(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ManHinhAdminResponse> createManHinh(@RequestBody ManHinhAdminRequest manHinhAdminRequest) {
        ManHinhAdminResponse response = manHinhAdminService.createManHinh(manHinhAdminRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManHinhAdminResponse> updateManHinh(@PathVariable Integer id, @RequestBody ManHinhAdminRequest manHinhAdminRequest) {
        ManHinhAdminResponse response = manHinhAdminService.updateManHinh(id, manHinhAdminRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ManHinhAdminResponse> deleteManHinh(@PathVariable Integer id) {
        ManHinhAdminResponse response = manHinhAdminService.deleteManHinh(id);

        return ResponseEntity.ok(response);
    }
}
