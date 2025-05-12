package org.example.websitetechworld.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.ManHinh;
import org.example.websitetechworld.Services.AdminServices.ManHinhAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/manHinh")
public class ManHinhAdminController {
    private final ManHinhAdminService manHinhAdminService;

    @GetMapping
    public ResponseEntity<Page<ManHinh>> getAllManHinh(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<ManHinh> manHinhs = manHinhAdminService.getAllManHinh(pageable);
        return ResponseEntity.ok(manHinhs);
    }
}
