package org.example.websitetechworld.Controller.AdminController.BaoHanhAdminController;

import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.BaoHanhAdminResponse;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiBaoHanh;
import org.example.websitetechworld.Services.AdminServices.BaoHanhAdminServices.BaoHanhAdminServices;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/bao-hanh")
public class BaoHanhAdminController {
    private final BaoHanhAdminServices baoHanhAdminServices;

    public BaoHanhAdminController(BaoHanhAdminServices baoHanhAdminServices) {
        this.baoHanhAdminServices = baoHanhAdminServices;
    }

    @GetMapping
    public ResponseEntity<Page<BaoHanhAdminResponse>> getAll (
            @RequestParam(required = false) String search,
            @RequestParam(required = false) TrangThaiBaoHanh trangThai,
            @RequestParam(required = false) LocalDate ngayBatDau,
            @RequestParam(required = false) LocalDate ngayKetThuc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(baoHanhAdminServices.getAllBaoHanh(search, trangThai, ngayBatDau, ngayKetThuc, page, size));
    }
}
