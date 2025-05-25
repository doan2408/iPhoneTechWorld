package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.RomAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.RomAdminResponse;
import org.example.websitetechworld.Entity.Rom;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.RomAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/rom")
public class RomAdminController {
    private final RomAdminService romAdminService;

    @GetMapping
    public ResponseEntity<Page<RomAdminResponse>> getRomAll(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<RomAdminResponse> roms = romAdminService.getAllRoms(pageable);

        return ResponseEntity.ok(roms);
    }

    @PostMapping
    public ResponseEntity<RomAdminResponse> createRom(@RequestBody RomAdminRequest romAdminRequest) {
        RomAdminResponse romAdminResponse = romAdminService.createRom(romAdminRequest);
        return ResponseEntity.ok(romAdminResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RomAdminResponse> detailRom(@PathVariable Integer id) {
        RomAdminResponse romAdminResponse = romAdminService.detailRom(id);
        return ResponseEntity.ok(romAdminResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RomAdminResponse> updateRom(@PathVariable Integer id, @RequestBody RomAdminRequest romAdminRequest) {
        RomAdminResponse romAdminResponse = romAdminService.updateRom(id, romAdminRequest);

        return ResponseEntity.ok(romAdminResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RomAdminResponse> deleteRom(@PathVariable Integer id) {
        RomAdminResponse romAdminResponse = romAdminService.deleteRom(id);
        return ResponseEntity.ok(romAdminResponse);
    }


}
