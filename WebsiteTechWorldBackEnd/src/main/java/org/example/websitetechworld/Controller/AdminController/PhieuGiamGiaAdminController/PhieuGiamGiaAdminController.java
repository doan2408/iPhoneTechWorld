package org.example.websitetechworld.Controller.AdminController.PhieuGiamGiaAdminController;

import jakarta.validation.Valid;
import org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest.PhieuGiamGiaAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices.PhieuGiamGiaAdminService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/phieu-giam-gia")
public class PhieuGiamGiaAdminController {

    private final PhieuGiamGiaAdminService phieuGiamGiaAdminService;

    public PhieuGiamGiaAdminController(PhieuGiamGiaAdminService phieuGiamGiaAdminService) {
        this.phieuGiamGiaAdminService = phieuGiamGiaAdminService;
    }

    @GetMapping
    public ResponseEntity<Page<PhieuGiamGiaAdminResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) TrangThaiPGG trangThai,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayBatDau,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayKetThuc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        phieuGiamGiaAdminService.capNhatTrangThaiPhieuGiamGia();
        Page<PhieuGiamGiaAdminResponse> result = phieuGiamGiaAdminService.layDanhSachPhieuGiamGia(
                search, trangThai, ngayBatDau, ngayKetThuc, page, size, sortBy, direction);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaAdminResponse> getPhieuGiamGia(@PathVariable int id) {
        PhieuGiamGiaAdminResponse response = phieuGiamGiaAdminService.layPhieuGiamGiaTheoId(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PhieuGiamGiaAdminResponse> addPhieuGiamGia(@Valid @RequestBody PhieuGiamGiaAdminRequest phieuGiamGiaAdminRequest) {
        PhieuGiamGiaAdminResponse created = phieuGiamGiaAdminService.themPhieuGiamGia(phieuGiamGiaAdminRequest);
        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaAdminResponse> updatePhieuGiamGia(
            @PathVariable int id, @Valid @RequestBody PhieuGiamGiaAdminRequest phieuGiamGiaAdminRequest) {
        PhieuGiamGiaAdminResponse updated = phieuGiamGiaAdminService.capNhatPhieuGiamGia(id, phieuGiamGiaAdminRequest);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePhieuGiamGia(@PathVariable int id) {
        String result = phieuGiamGiaAdminService.xoaPhieuGiamGia(id);
        return ResponseEntity.ok(result);
    }

//    @GetMapping("/san-pham")
//    public ResponseEntity<Page<SanPhamChiTietResponse>> getAllKhachHang(
//            @RequestParam(required = false) String search,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size) {
//        Page<SanPhamChiTietResponse> danhSachSanPham = phieuGiamGiaAdminService.layDanhSachSanPham(search, page, size);
//        return ResponseEntity.ok(danhSachSanPham);
//    }
}