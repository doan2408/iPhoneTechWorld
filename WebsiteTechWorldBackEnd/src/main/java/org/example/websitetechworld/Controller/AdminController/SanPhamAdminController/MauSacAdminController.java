package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.MauSacAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.MauSacQuickAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.MauSacAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.MauSacAdminService;
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
@RequestMapping("/admin/mauSac")
@Validated
public class MauSacAdminController {
    private final MauSacAdminService mauSacService;

    @GetMapping
    public ResponseEntity<Page<MauSacAdminResponse>> getAllMauSac(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<MauSacAdminResponse> mauSacs = mauSacService.getAllMauSac(pageable);
        return ResponseEntity.ok(mauSacs);
    }

    @GetMapping("/listMauSac")
    public ResponseEntity<List<MauSacAdminResponse>> getAllMauSacList() {
        List<MauSacAdminResponse> mauSacs = mauSacService.getAllMauSacList();
        return ResponseEntity.ok(mauSacs);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<MauSacAdminResponse>> searchMauSac(
            @RequestParam(required = false) String search,
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<MauSacAdminResponse> mauSacs = mauSacService.searchMauSac(search, pageable);
        return ResponseEntity.ok(mauSacs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MauSacAdminResponse> detailMauSac(@PathVariable Integer id) {
        MauSacAdminResponse response = mauSacService.detailMauSac(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<MauSacAdminResponse> createMauSac(@Valid @RequestBody MauSacAdminRequest mauSacAdminRequest) {
        MauSacAdminResponse response = mauSacService.createMauSac(mauSacAdminRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/quick-mauSac")
    public ResponseEntity<MauSacAdminResponse> createMauSacQuick(@Valid @RequestBody MauSacQuickAdminRequest mauSacAdminRequest) {
        MauSacAdminResponse response = mauSacService.createMauSacQuick(mauSacAdminRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MauSacAdminResponse> updateMauSac(@PathVariable Integer id, @Valid @RequestBody MauSacAdminRequest mauSacAdminRequest) {
        MauSacAdminResponse response = mauSacService.updateMauSac(id, mauSacAdminRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MauSacAdminResponse> deleteMauSac(@PathVariable Integer id) {
        MauSacAdminResponse response = mauSacService.deleteMauSac(id);
        return ResponseEntity.ok(response);
    }
}