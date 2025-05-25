package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.LoaiAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ManHinhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.LoaiAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ManHinhAdminResponse;
import org.example.websitetechworld.Entity.Loai;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.LoaiAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/loai")
public class LoaiAdminController {
    private final LoaiAdminService loaiAdminService;

    @GetMapping
    public ResponseEntity<Page<LoaiAdminResponse>> getAllLoai(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<LoaiAdminResponse> loais = loaiAdminService.getAllLoai(pageable);

        return ResponseEntity.ok(loais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoaiAdminResponse> detailLoai(@PathVariable Integer id) {
        LoaiAdminResponse response = loaiAdminService.detailLoai(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<LoaiAdminResponse> createLoai(@RequestBody LoaiAdminRequest loaiAdminRequest) {
        LoaiAdminResponse response = loaiAdminService.createLoai(loaiAdminRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoaiAdminResponse> updateLoai(@PathVariable Integer id, @RequestBody LoaiAdminRequest loaiAdminRequest) {
        LoaiAdminResponse response = loaiAdminService.updateLoai(id, loaiAdminRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LoaiAdminResponse> deleteLoai(@PathVariable Integer id) {
        LoaiAdminResponse response = loaiAdminService.deleteLoai(id);

        return ResponseEntity.ok(response);
    }
}
