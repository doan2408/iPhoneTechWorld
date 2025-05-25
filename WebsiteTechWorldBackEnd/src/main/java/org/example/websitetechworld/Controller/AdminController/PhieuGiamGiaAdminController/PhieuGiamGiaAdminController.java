package org.example.websitetechworld.Controller.AdminController.PhieuGiamGiaAdminController;

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
@RequestMapping ("/admin/phieu-giam-gia")
public class PhieuGiamGiaAdminController {

    private final PhieuGiamGiaAdminServices phieuGiamGiaAdminServices;

    public PhieuGiamGiaAdminController(PhieuGiamGiaAdminServices phieuGiamGiaAdminServices) {
        this.phieuGiamGiaAdminServices = phieuGiamGiaAdminServices;
    }

    @GetMapping
    public ResponseEntity<Page<PhieuGiamGiaAdminResponse>> getAll (
            @RequestParam(required = false) String search,
            @RequestParam(required = false) TrangThaiPGG trangThai,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayBatDau,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayKetThuc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<PhieuGiamGiaAdminResponse> result = phieuGiamGiaAdminServices.getPagePhieuGiamGia(
                search, trangThai, ngayBatDau, ngayKetThuc, page, size, sortBy, direction
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping ("/{id}")
    public PhieuGiamGiaAdminResponse getPhieuGiamGia (
            @PathVariable int id
    ) {
        return phieuGiamGiaAdminServices.getPhieuGiamGia(id);
    }

    @PostMapping
    public PhieuGiamGiaAdminResponse addPhieuGiamGia (
            @RequestBody PhieuGiamGiaAdminResponse phieuGiamGiaResponse
    ) {
        return phieuGiamGiaAdminServices.addPhieuGiamGia(phieuGiamGiaResponse);
    }

    @PutMapping ("/{id}")
    public PhieuGiamGiaAdminResponse updatePhieuGiamGia (
            @PathVariable int id,
            @RequestBody PhieuGiamGiaAdminResponse phieuGiamGiaResponse
    ) {
        return phieuGiamGiaAdminServices.updatePhieuGiamGia(id, phieuGiamGiaResponse);
    }

    @DeleteMapping ("/{id}")
    public String deletePhieuGiamGia (
            @PathVariable int id
    ) {
        return phieuGiamGiaAdminServices.deletePhieuGiamGia(id);
    }
}
