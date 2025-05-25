package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CameraSauAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CameraTruocAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CameraSauAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CameraTruocAdminResponse;
import org.example.websitetechworld.Entity.CameraSau;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.CameraSauAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cameraSau")
public class CameraSauAdminController {
    private final CameraSauAdminService cameraSauAdminService;

    @GetMapping
    public ResponseEntity<Page<CameraSauAdminResponse>> getAllCameraSau(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<CameraSauAdminResponse> cameraSaus = cameraSauAdminService.getAllCameraSau(pageable);
        return ResponseEntity.ok(cameraSaus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CameraSauAdminResponse> detailCameraSau(@PathVariable Integer id) {
        CameraSauAdminResponse response = cameraSauAdminService.detailCameraSau(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CameraSauAdminResponse> createCameraSau(@RequestBody CameraSauAdminRequest cameraSauAdminRequest) {
        CameraSauAdminResponse response = cameraSauAdminService.createCameraSau(cameraSauAdminRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CameraSauAdminResponse> updateCameraSau(@PathVariable Integer id, @RequestBody CameraSauAdminRequest cameraSauAdminRequest) {
        CameraSauAdminResponse response = cameraSauAdminService.updateCameraSau(id, cameraSauAdminRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CameraSauAdminResponse> deleteCameraSau(@PathVariable Integer id) {
        CameraSauAdminResponse response = cameraSauAdminService.deleteCameraSau(id);

        return ResponseEntity.ok(response);
    }
}
