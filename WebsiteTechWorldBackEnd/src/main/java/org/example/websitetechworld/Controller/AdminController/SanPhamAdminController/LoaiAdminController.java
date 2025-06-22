package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.LoaiAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.LoaiAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.LoaiAdminService;
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
@RequestMapping("/admin/loai")
@Validated
public class LoaiAdminController {
    private final LoaiAdminService loaiAdminService;

    @GetMapping
    public ResponseEntity<Page<LoaiAdminResponse>> getAllLoai(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<LoaiAdminResponse> loais = loaiAdminService.getAllLoai(pageable);
        return ResponseEntity.ok(loais);
    }

    @GetMapping("/listLoai")
    public ResponseEntity<List<LoaiAdminResponse>> getAllLoaiList() {
        List<LoaiAdminResponse> loais = loaiAdminService.getAllLoaiList();
        return ResponseEntity.ok(loais);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<LoaiAdminResponse>> searchLoai(
            @RequestParam(required = false) String search,
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<LoaiAdminResponse> loais = loaiAdminService.searchLoai(search, pageable);
        return ResponseEntity.ok(loais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoaiAdminResponse> detailLoai(@PathVariable Integer id) {
        LoaiAdminResponse response = loaiAdminService.detailLoai(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<LoaiAdminResponse> createLoai(@Valid @RequestBody LoaiAdminRequest loaiAdminRequest) {
        LoaiAdminResponse response = loaiAdminService.createLoai(loaiAdminRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoaiAdminResponse> updateLoai(@PathVariable Integer id, @Valid @RequestBody LoaiAdminRequest loaiAdminRequest) {
        LoaiAdminResponse response = loaiAdminService.updateLoai(id, loaiAdminRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LoaiAdminResponse> deleteLoai(@PathVariable Integer id) {
        LoaiAdminResponse response = loaiAdminService.deleteLoai(id);
        return ResponseEntity.ok(response);
    }
}