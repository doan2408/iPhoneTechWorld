package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.Rom;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.RomAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/rom")
public class RomAdminController {
    private final RomAdminService romAdminService;

    @GetMapping
    public ResponseEntity<Page<Rom>> getRomAll(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<Rom> roms = romAdminService.getAllRoms(pageable);

        return ResponseEntity.ok(roms);
    }
}
