package org.example.websitetechworld.Controller.AdminController.KhuyenMaiAdminController;

import org.example.websitetechworld.Dto.Request.AdminRequest.KhuyenMaiAdminRequest.KhuyenMaiAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.KhuyenMaiAdminResponse.KhuyenMaiAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Enum.KhuyenMai.TrangThaiKhuyenMai;
import org.example.websitetechworld.Services.AdminServices.KhuyenMaiAdminService.KhuyenMaiAdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/khuyen-mai")
public class KhuyenMaiAdminController {
    private final KhuyenMaiAdminService khuyenMaiAdminService;

    public KhuyenMaiAdminController(KhuyenMaiAdminService khuyenMaiAdminService) {
        this.khuyenMaiAdminService = khuyenMaiAdminService;
    }

    @GetMapping
    public ResponseEntity<Page<KhuyenMaiAdminResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) TrangThaiKhuyenMai trangThai,
            @RequestParam(required = false) LocalDate ngayBatDau,
            @RequestParam(required = false) LocalDate ngayKetThuc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<KhuyenMaiAdminResponse> result = khuyenMaiAdminService.getAllKhuyenMai(search, trangThai, ngayBatDau, ngayKetThuc, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KhuyenMaiAdminResponse> getById(@PathVariable Integer id) {
        KhuyenMaiAdminResponse result = khuyenMaiAdminService.getKhuyenMaiById(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<KhuyenMaiAdminResponse> create(@Valid @RequestBody KhuyenMaiAdminRequest request) {
        KhuyenMaiAdminResponse result = khuyenMaiAdminService.createKhuyenMai(request);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<KhuyenMaiAdminResponse> update(@PathVariable Integer id, @Valid @RequestBody KhuyenMaiAdminRequest request) {
        KhuyenMaiAdminResponse result = khuyenMaiAdminService.updateKhuyenMai(id, request);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        khuyenMaiAdminService.deleteKhuyenMai(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/san-pham")
    public ResponseEntity<List<SanPhamAdminResponse>> getAllSanPham (
            @RequestParam(required = false) String search) {
        List<SanPhamAdminResponse> danhSachSanPham = khuyenMaiAdminService.layDanhSachSanPham(search);
        return ResponseEntity.ok(danhSachSanPham);
    }

    @PostMapping("/san-pham-chi-tiet")
    public ResponseEntity<List<SanPhamChiTietResponse>> getSanPhamChiTietsBySanPhamIds(@RequestBody List<Integer> sanPhamIds) {
        try {
            List<SanPhamChiTietResponse> sanPhamChiTiets = khuyenMaiAdminService.getSanPhamChiTietsBySanPhamIds(sanPhamIds);
            return ResponseEntity.ok(sanPhamChiTiets);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/san-pham-chi-tiet/{id}")
    public ResponseEntity<List<SanPhamChiTietResponse>> getSanPhamChiTietByIdKhuyenMai (@PathVariable Integer id) {
        List<SanPhamChiTietResponse> result = khuyenMaiAdminService.getSanPhamChiTietByIdKhuyenMai(id);
        return ResponseEntity.ok(result);
    }
}
