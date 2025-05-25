package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HeDieuHanhAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HinhAnhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HeDieuHanhAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HinhAnhAdminResponse;
import org.example.websitetechworld.Entity.HeDieuHanh;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.HeDieuHanhAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/heDieuHanh")
public class HeDieuHanhAdminController {

    private final HeDieuHanhAdminService heDieuHanhAdminService;

    @GetMapping
    public ResponseEntity<Page<HeDieuHanhAdminResponse>> getAllHeDieuHanh(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<HeDieuHanhAdminResponse> heDieuHanhs = heDieuHanhAdminService.getAllHeDieuHanh(pageable);

        return ResponseEntity.ok(heDieuHanhs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeDieuHanhAdminResponse> detailHinhAnh(@PathVariable Integer id) {
        HeDieuHanhAdminResponse response = heDieuHanhAdminService.detailHeDieuHanh(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<HeDieuHanhAdminResponse> createHinhAnh(@RequestBody HeDieuHanhAdminRequest heDieuHanhAdminRequest) {
        HeDieuHanhAdminResponse response = heDieuHanhAdminService.createHeDieuHanh(heDieuHanhAdminRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HeDieuHanhAdminResponse> updateHinhAnh(@PathVariable Integer id, @RequestBody HeDieuHanhAdminRequest heDieuHanhAdminRequest) {
        HeDieuHanhAdminResponse response = heDieuHanhAdminService.updateHeDieuHanh(id, heDieuHanhAdminRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HeDieuHanhAdminResponse> deleteHinhAnh(@PathVariable Integer id) {
        HeDieuHanhAdminResponse response = heDieuHanhAdminService.deleteHeDieuHanh(id);

        return ResponseEntity.ok(response);
    }
}
