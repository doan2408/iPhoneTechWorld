package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.NhaCungCapAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.NhaCungCapAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.NhaCungCapAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/nhaCungCap")
public class NhaCungCapAdminController {
    private final NhaCungCapAdminService nhaCungCapAdminService;

    @GetMapping
    public ResponseEntity<Page<NhaCungCapAdminResponse>> getAllNhaCungCap(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<NhaCungCapAdminResponse> nhaCungCaps = nhaCungCapAdminService.getAllNhaCungCap(pageable);
        return ResponseEntity.ok(nhaCungCaps);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NhaCungCapAdminResponse> detailNhaCungCap(@PathVariable Integer id) {
        NhaCungCapAdminResponse response = nhaCungCapAdminService.detailNCC(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<NhaCungCapAdminResponse> createNhaCungCap(@RequestBody NhaCungCapAdminRequest nhaCungCapAdminRequest) {
        NhaCungCapAdminResponse response = nhaCungCapAdminService.createNCC(nhaCungCapAdminRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NhaCungCapAdminResponse> updateNhaCungCap(@PathVariable Integer id, @RequestBody NhaCungCapAdminRequest nhaCungCapAdminRequest) {
        NhaCungCapAdminResponse response = nhaCungCapAdminService.updateNCC(id, nhaCungCapAdminRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NhaCungCapAdminResponse> deleteNhaCungCap(@PathVariable Integer id) {
        NhaCungCapAdminResponse response = nhaCungCapAdminService.deleteNCC(id);

        return ResponseEntity.ok(response);
    }
}
