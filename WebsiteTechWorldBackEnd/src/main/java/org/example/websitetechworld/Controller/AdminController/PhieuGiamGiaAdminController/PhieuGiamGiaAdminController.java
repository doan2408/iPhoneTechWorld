package org.example.websitetechworld.Controller.AdminController.PhieuGiamGiaAdminController;

import jakarta.validation.Valid;
import org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest.PhieuGiamGiaAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.KhachHangGiamGiaResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices.PhieuGiamGiaAdminServices;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/phieu-giam-gia")
public class PhieuGiamGiaAdminController {

    private final PhieuGiamGiaAdminServices phieuGiamGiaAdminServices;

    public PhieuGiamGiaAdminController(PhieuGiamGiaAdminServices phieuGiamGiaAdminServices) {
        this.phieuGiamGiaAdminServices = phieuGiamGiaAdminServices;
    }

    @GetMapping
    public ResponseEntity<Page<PhieuGiamGiaAdminResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) TrangThaiPGG trangThai,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayBatDau,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayKetThuc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        phieuGiamGiaAdminServices.updateTrangThaiPhieuGiamGia();
        Page<PhieuGiamGiaAdminResponse> result = phieuGiamGiaAdminServices.getPagePhieuGiamGia(
                search, trangThai, ngayBatDau, ngayKetThuc, page, size, sortBy, direction);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaAdminResponse> getPhieuGiamGia(@PathVariable int id) {
        PhieuGiamGiaAdminResponse response = phieuGiamGiaAdminServices.getPhieuGiamGia(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PhieuGiamGiaAdminResponse> addPhieuGiamGia(@Valid @RequestBody PhieuGiamGiaAdminRequest phieuGiamGiaAdminRequest) {
        PhieuGiamGiaAdminResponse created = phieuGiamGiaAdminServices.addPhieuGiamGia(phieuGiamGiaAdminRequest);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaAdminResponse> updatePhieuGiamGia(
            @PathVariable int id, @Valid @RequestBody PhieuGiamGiaAdminRequest phieuGiamGiaAdminRequest) {
        PhieuGiamGiaAdminResponse updated = phieuGiamGiaAdminServices.updatePhieuGiamGia(id, phieuGiamGiaAdminRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePhieuGiamGia(@PathVariable int id) {
        String result = phieuGiamGiaAdminServices.deletePhieuGiamGia(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/khach-hang")
    public ResponseEntity<List<KhachHangGiamGiaResponse>> getAllKhachHang() {
        List<KhachHangGiamGiaResponse> khachHangList = phieuGiamGiaAdminServices.getAllKhachHang();
        return ResponseEntity.ok(khachHangList);
    }
}