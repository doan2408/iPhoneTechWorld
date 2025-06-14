package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.NhaCungCapAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.NhaCungCapQuickCreateAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.NhaCungCapAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.NhaCungCapAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/nhaCungCap")
@Validated
public class NhaCungCapAdminController {
    private final NhaCungCapAdminService nhaCungCapAdminService;

    @GetMapping
    public ResponseEntity<Page<NhaCungCapAdminResponse>> getAllNhaCungCap(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<NhaCungCapAdminResponse> nhaCungCaps = nhaCungCapAdminService.getAllNhaCungCap(pageable);
        return ResponseEntity.ok(nhaCungCaps);
    }

    @GetMapping("/listNCC")
    public ResponseEntity<List<NhaCungCapAdminResponse>> getAllNhaCungCapList() {
        List<NhaCungCapAdminResponse> nhaCungCaps = nhaCungCapAdminService.getAllNhaCungCapList();
        return ResponseEntity.ok(nhaCungCaps);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<NhaCungCapAdminResponse>> searchNhaCungCap(
            @RequestParam(required = false) String search,
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<NhaCungCapAdminResponse> nhaCungCaps = nhaCungCapAdminService.searchNhaCungCap(search, pageable);
        return ResponseEntity.ok(nhaCungCaps);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NhaCungCapAdminResponse> detailNhaCungCap(@PathVariable Integer id) {
        NhaCungCapAdminResponse response = nhaCungCapAdminService.detailNCC(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<NhaCungCapAdminResponse> createNhaCungCap(@Valid @RequestBody NhaCungCapAdminRequest nhaCungCapAdminRequest) {
        NhaCungCapAdminResponse response = nhaCungCapAdminService.createNCC(nhaCungCapAdminRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/quick-create")
    public ResponseEntity<NhaCungCapAdminResponse> quickCreateNhaCungCap(@Valid @RequestBody NhaCungCapQuickCreateAdminRequest req) {
        NhaCungCapAdminResponse response = nhaCungCapAdminService.quickCreateNCC(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NhaCungCapAdminResponse> updateNhaCungCap(@PathVariable Integer id, @Valid @RequestBody NhaCungCapAdminRequest nhaCungCapAdminRequest) {
        NhaCungCapAdminResponse response = nhaCungCapAdminService.updateNCC(id, nhaCungCapAdminRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NhaCungCapAdminResponse> deleteNhaCungCap(@PathVariable Integer id) {
        NhaCungCapAdminResponse response = nhaCungCapAdminService.deleteNCC(id);
        return ResponseEntity.ok(response);
    }
}