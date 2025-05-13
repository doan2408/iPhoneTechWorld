package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HinhAnhAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.HinhAnhAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hinhAnh")
public class HinhAnhAdminController {
    private final HinhAnhAdminService hinhAnhAdminService;

    @GetMapping
    public ResponseEntity<Page<HinhAnhAdminResponse>> getAllHinhAnh(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<HinhAnhAdminResponse> hinhAnhs = hinhAnhAdminService.getAllHinhAnh(pageable);
        return ResponseEntity.ok(hinhAnhs);
    }
}
