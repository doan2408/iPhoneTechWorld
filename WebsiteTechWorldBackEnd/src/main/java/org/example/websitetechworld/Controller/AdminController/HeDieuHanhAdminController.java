package org.example.websitetechworld.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.HeDieuHanh;
import org.example.websitetechworld.Services.AdminServices.HeDieuHanhAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/heDieuHanh")
public class HeDieuHanhAdminController {

    private final HeDieuHanhAdminService heDieuHanhAdminService;

    @GetMapping
    public ResponseEntity<Page<HeDieuHanh>> getAllHeDieuHanh(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<HeDieuHanh> heDieuHanhs = heDieuHanhAdminService.getAllHeDieuHanh(pageable);

        return ResponseEntity.ok(heDieuHanhs);
    }
}
