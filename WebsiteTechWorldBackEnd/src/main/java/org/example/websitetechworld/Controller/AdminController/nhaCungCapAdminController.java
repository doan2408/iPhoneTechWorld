package org.example.websitetechworld.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.NhaCungCap;
import org.example.websitetechworld.Services.AdminServices.NhaCungCapAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/nhaCungCap")
public class nhaCungCapAdminController {
    private final NhaCungCapAdminService nhaCungCapAdminService;

    @GetMapping
    public ResponseEntity<Page<NhaCungCap>> getAllNhaCungCap(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<NhaCungCap> nhaCungCaps = nhaCungCapAdminService.getAllNhaCungCap(pageable);
        return ResponseEntity.ok(nhaCungCaps);
    }
}
