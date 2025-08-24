package org.example.websitetechworld.Controller.AdminController.KhuyenMaiAdminController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.KhuyenMaiAdminRequest.KhuyenMaiAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.KhuyenMaiAdminRequest.RemoveProductRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.KhuyenMaiAdminResponse.KhuyenMaiAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Enum.KhuyenMai.TrangThaiKhuyenMai;
import org.example.websitetechworld.Services.AdminServices.KhuyenMaiAdminService.KhuyenMaiAdminService;
import org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices.SchedulerService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/khuyen-mai")
@RequiredArgsConstructor
public class KhuyenMaiAdminController {

    private final KhuyenMaiAdminService khuyenMaiAdminService;
    private final SchedulerService schedulerService;

    @GetMapping
    public ResponseEntity<Page<KhuyenMaiAdminResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) TrangThaiKhuyenMai trangThai,
            @RequestParam(required = false) LocalDate ngayBatDau,
            @RequestParam(required = false) LocalDate ngayKetThuc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(khuyenMaiAdminService.getAllKhuyenMai(search, trangThai, ngayBatDau, ngayKetThuc, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<KhuyenMaiAdminResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(khuyenMaiAdminService.getKhuyenMaiById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<KhuyenMaiAdminResponse> create(@Valid @RequestBody KhuyenMaiAdminRequest request) {
        return new ResponseEntity<>(khuyenMaiAdminService.createKhuyenMai(request), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<KhuyenMaiAdminResponse> update(@PathVariable Integer id, @Valid @RequestBody KhuyenMaiAdminRequest request) {
        return ResponseEntity.ok(khuyenMaiAdminService.updateKhuyenMai(id, request));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        khuyenMaiAdminService.deleteKhuyenMai(id);
        return ResponseEntity.noContent().build();
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/san-pham")
    public ResponseEntity<List<SanPhamAdminResponse>> getAllSanPham(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filter) {
        return ResponseEntity.ok(khuyenMaiAdminService.layDanhSachSanPham(search, filter));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/san-pham-chi-tiet")
    public ResponseEntity<List<SanPhamChiTietResponse>> getSanPhamChiTietsBySanPhamIds(@RequestBody List<Integer> sanPhamIds) {
        return ResponseEntity.ok(khuyenMaiAdminService.getSanPhamChiTietsBySanPhamIds(sanPhamIds));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/san-pham-chi-tiet/{id}")
    public ResponseEntity<List<SanPhamChiTietResponse>> getSanPhamChiTietByIdKhuyenMai(@PathVariable Integer id) {
        return ResponseEntity.ok(khuyenMaiAdminService.getSanPhamChiTietByIdKhuyenMai(id));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/san-pham-chi-tiet/{sanPhamChiTietId}/promotions")
    public ResponseEntity<List<KhuyenMaiAdminResponse>> getExistingPromotions(@PathVariable Integer sanPhamChiTietId) {
        return ResponseEntity.ok(khuyenMaiAdminService.getExistingPromotions(sanPhamChiTietId));
    }

    @GetMapping("/next-delay")
    public Map<String, Long> getNextDelay() {
        LocalDateTime now = LocalDateTime.now();
        Duration nextDelay = schedulerService.calculateNextDelayKhuyenMai(now);
        return Map.of("delay", nextDelay.toMillis());
    }

    @PostMapping("/update-statuses")
    public Map<String, String> updateStatuses() {
        schedulerService.runTaskKhuyenMai();
        return Map.of("status", "success");
    }
}
