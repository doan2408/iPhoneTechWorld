package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.XuatXu;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.XuatXuAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/xuatXu")
public class XuatXuAdminController {
    private final XuatXuAdminService xuatXuAdminService;

    @GetMapping
    public ResponseEntity<Page<XuatXu>> getXuatXuResponse(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<XuatXu> xuatXus = xuatXuAdminService.getAllXuatXu(pageable);
        return ResponseEntity.ok(xuatXus);
    }
}
