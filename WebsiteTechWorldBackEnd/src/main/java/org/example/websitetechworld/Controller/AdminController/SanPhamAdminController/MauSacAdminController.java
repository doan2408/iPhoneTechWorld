package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.MauSacAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.NhaCungCapAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.MauSacAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.NhaCungCapAdminResponse;
import org.example.websitetechworld.Entity.MauSac;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.MauSacAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/mauSac")
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

    @GetMapping("/{id}")
    public ResponseEntity<MauSacAdminResponse> detailMauSac(@PathVariable Integer id) {
        MauSacAdminResponse response = mauSacService.detailMauSac(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<MauSacAdminResponse> createMauSac(@RequestBody MauSacAdminRequest mauSacAdminRequest) {
        MauSacAdminResponse response = mauSacService.createMauSac(mauSacAdminRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MauSacAdminResponse> updateMauSac(@PathVariable Integer id, @RequestBody MauSacAdminRequest mauSacAdminRequest) {
        MauSacAdminResponse response = mauSacService.updateMauSac(id, mauSacAdminRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MauSacAdminResponse> deleteMauSac(@PathVariable Integer id) {
        MauSacAdminResponse response = mauSacService.deleteMauSac(id);

        return ResponseEntity.ok(response);
    }
}
