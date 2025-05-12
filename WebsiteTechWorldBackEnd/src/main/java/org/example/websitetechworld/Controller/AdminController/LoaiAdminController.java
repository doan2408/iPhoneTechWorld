package org.example.websitetechworld.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.Loai;
import org.example.websitetechworld.Services.AdminServices.LoaiAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/loai")
public class LoaiAdminController {
    private final LoaiAdminService loaiAdminService;

    @GetMapping
    public ResponseEntity<Page<Loai>> getAllLoai(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<Loai> loais = loaiAdminService.getAllLoai(pageable);

        return ResponseEntity.ok(loais);
    }
}
