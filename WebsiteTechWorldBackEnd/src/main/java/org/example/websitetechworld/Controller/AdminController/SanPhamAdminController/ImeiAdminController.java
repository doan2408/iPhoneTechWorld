package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ImeiAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.LoaiAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ImeiAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.LoaiAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.ImeiAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/imei")
public class ImeiAdminController {
    private final ImeiAdminService imeiAdminService;

    @GetMapping
    public ResponseEntity<Page<ImeiAdminResponse>> getAllImei(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<ImeiAdminResponse> imeis = imeiAdminService.getAllImei(pageable);

        return ResponseEntity.ok(imeis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImeiAdminResponse> detailImei(@PathVariable Integer id) {
        ImeiAdminResponse response = imeiAdminService.detailImei(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ImeiAdminResponse> createImei(@RequestBody ImeiAdminRequest imeiAdminRequest) {
        ImeiAdminResponse response = imeiAdminService.createImei(imeiAdminRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImeiAdminResponse> updateImei(@PathVariable Integer id, @RequestBody ImeiAdminRequest imeiAdminRequest) {
        ImeiAdminResponse response = imeiAdminService.updateImei(id, imeiAdminRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ImeiAdminResponse> deleteImei(@PathVariable Integer id) {
        ImeiAdminResponse response = imeiAdminService.deleteImei(id);

        return ResponseEntity.ok(response);
    }
}
