package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.RamAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.RamQuickCreateAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.RamAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.RamAdminService;
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
@RequestMapping("/admin/ram")
@Validated
public class RamAdminController {
    private final RamAdminService ramAdminService;

    @GetMapping
    public ResponseEntity<Page<RamAdminResponse>> getAllRam(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<RamAdminResponse> rams = ramAdminService.getAllRam(pageable);
        return ResponseEntity.ok(rams);
    }

    @GetMapping("/listRam")
    public ResponseEntity<List<RamAdminResponse>> getAllRamList() {
        List<RamAdminResponse> rams = ramAdminService.getAllRamList();
        return ResponseEntity.ok(rams);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RamAdminResponse>> searchRams(
            @RequestParam(required = false) String search,
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<RamAdminResponse> rams = ramAdminService.searchRams(search, pageable);
        return ResponseEntity.ok(rams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RamAdminResponse> detailRam(@PathVariable Integer id) {
        RamAdminResponse ramAdminResponse = ramAdminService.detailRam(id);
        return ResponseEntity.ok(ramAdminResponse);
    }

    @PostMapping
    public ResponseEntity<RamAdminResponse> createRam(@Valid @RequestBody RamAdminRequest ramAdminRequest) {
        RamAdminResponse ramAdminResponse = ramAdminService.createRam(ramAdminRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ramAdminResponse);
    }

    @PostMapping("/quick-ram")
    public ResponseEntity<RamAdminResponse> createRamQuick(@Valid @RequestBody RamQuickCreateAdminRequest ramAdminRequest) {
        RamAdminResponse ramAdminResponse = ramAdminService.createRamQuick(ramAdminRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ramAdminResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RamAdminResponse> updateRam(@PathVariable Integer id, @Valid @RequestBody RamAdminRequest ramAdminRequest) {
        RamAdminResponse ramAdminResponse = ramAdminService.updateRam(id, ramAdminRequest);
        return ResponseEntity.ok(ramAdminResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RamAdminResponse> deleteRam(@PathVariable Integer id) {
        RamAdminResponse ramAdminResponse = ramAdminService.deleteRam(id);
        return ResponseEntity.ok(ramAdminResponse);
    }
}