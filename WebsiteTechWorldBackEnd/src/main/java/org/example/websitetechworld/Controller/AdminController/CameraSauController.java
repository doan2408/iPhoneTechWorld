package org.example.websitetechworld.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.CameraSau;
import org.example.websitetechworld.Services.AdminServices.CameraSauAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cameraSau")
public class CameraSauController {
    private final CameraSauAdminService cameraSauAdminService;

    @GetMapping
    public ResponseEntity<Page<CameraSau>> getAllCameraSau(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<CameraSau> cameraSaus = cameraSauAdminService.getAllCameraSau(pageable);
        return ResponseEntity.ok(cameraSaus);
    }
}
