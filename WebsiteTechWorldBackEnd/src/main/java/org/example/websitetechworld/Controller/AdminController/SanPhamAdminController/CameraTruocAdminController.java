package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.CameraTruoc;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.CameraTruocAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cameraTruoc")
public class CameraTruocAdminController {
    private final CameraTruocAdminService cameraTruocAdminService;

    @GetMapping
    public ResponseEntity<Page<CameraTruoc>> getAllCameraTruoc(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<CameraTruoc> cameraTruocs = cameraTruocAdminService.getAllCameraTruoc(pageable);
        return ResponseEntity.ok(cameraTruocs);
    }
}
