package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HinhAnhAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ImeiAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HinhAnhAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ImeiAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.HinhAnhAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hinhAnh")
public class HinhAnhAdminController {
    private final HinhAnhAdminService hinhAnhAdminService;

    @GetMapping
    public ResponseEntity<Page<HinhAnhAdminResponse>> getAllHinhAnh(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<HinhAnhAdminResponse> hinhAnhs = hinhAnhAdminService.getAllHinhAnh(pageable);
        return ResponseEntity.ok(hinhAnhs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HinhAnhAdminResponse> detailHinhAnh(@PathVariable Integer id) {
        HinhAnhAdminResponse response = hinhAnhAdminService.detailHinhAnh(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<HinhAnhAdminResponse> createHinhAnh(@RequestBody HinhAnhAdminRequest hinhAnhAdminRequest) {
        HinhAnhAdminResponse response = hinhAnhAdminService.createHinhAnh(hinhAnhAdminRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HinhAnhAdminResponse> updateHinhAnh(@PathVariable Integer id, @RequestBody HinhAnhAdminRequest hinhAnhAdminRequest) {
        HinhAnhAdminResponse response = hinhAnhAdminService.updateHinhAnh(id, hinhAnhAdminRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HinhAnhAdminResponse> deleteHinhAnh(@PathVariable Integer id) {
        HinhAnhAdminResponse response = hinhAnhAdminService.deleteHinhAnh(id);

        return ResponseEntity.ok(response);
    }
}
