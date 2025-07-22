package org.example.websitetechworld.Controller.AdminController.DanhGiaSanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.AdminResponse.DanhGiaSanPhamAdminResponse.DanhGiaSanPhamAdminResponse;
import org.example.websitetechworld.Services.AdminServices.DanhGiaSanPhamAdminService.DanhGiaSanPhamAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/danh-gia-san-pham")
public class DanhGiaSanPhamAdminController {
    private final DanhGiaSanPhamAdminService danhGiaSanPhamAdminService;

    @GetMapping
    public ResponseEntity<Page<DanhGiaSanPhamAdminResponse>> getAllDanhGiaAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("ngayDanhGia").descending());
        Page<DanhGiaSanPhamAdminResponse> result = danhGiaSanPhamAdminService.layTatCaDanhGiaAdmin(pageable);
        return ResponseEntity.ok(result);
    }
}
