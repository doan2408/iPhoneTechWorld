package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.Ram;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.RamAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/ram")
public class RamAdminController {
    private final RamAdminService ramAdminService;

    @GetMapping
    public ResponseEntity<Page<Ram>> getAllRam(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<Ram> rams = ramAdminService.getAllRam(pageable);

        return ResponseEntity.ok(rams);
    }
}
