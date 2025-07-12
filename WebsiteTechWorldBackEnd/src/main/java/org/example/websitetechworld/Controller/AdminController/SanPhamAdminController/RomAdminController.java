package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.RomAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.RomQuickAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.RomAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.RomAdminService;
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
@RequestMapping("/admin/rom")
@Validated
public class RomAdminController {
    private final RomAdminService romAdminService;

    @GetMapping
    public ResponseEntity<Page<RomAdminResponse>> getRomAll(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<RomAdminResponse> roms = romAdminService.getAllRoms(pageable);
        return ResponseEntity.ok(roms);
    }

    @GetMapping("/listRom")
    public ResponseEntity<List<RomAdminResponse>> getRomAllList() {
        List<RomAdminResponse> roms = romAdminService.getAllRomsList();
        return ResponseEntity.ok(roms);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RomAdminResponse>> searchRoms(
            @RequestParam(required = false) String search,
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<RomAdminResponse> roms = romAdminService.searchRoms(search, pageable);
        return ResponseEntity.ok(roms);
    }

    @PostMapping
    public ResponseEntity<RomAdminResponse> createRom(@Valid @RequestBody RomAdminRequest romAdminRequest) {
        RomAdminResponse romAdminResponse = romAdminService.createRom(romAdminRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(romAdminResponse);
    }

    @PostMapping("/quick-rom")
    public ResponseEntity<RomAdminResponse> createRomQuick(@Valid @RequestBody RomQuickAdminRequest romAdminRequest) {
        RomAdminResponse romAdminResponse = romAdminService.createRomQuick(romAdminRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(romAdminResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RomAdminResponse> detailRom(@PathVariable Integer id) {
        RomAdminResponse romAdminResponse = romAdminService.detailRom(id);
        return ResponseEntity.ok(romAdminResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RomAdminResponse> updateRom(@PathVariable Integer id, @Valid @RequestBody RomAdminRequest romAdminRequest) {
        RomAdminResponse romAdminResponse = romAdminService.updateRom(id, romAdminRequest);
        return ResponseEntity.ok(romAdminResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RomAdminResponse> deleteRom(@PathVariable Integer id) {
        RomAdminResponse romAdminResponse = romAdminService.deleteRom(id);
        return ResponseEntity.ok(romAdminResponse);
    }
}