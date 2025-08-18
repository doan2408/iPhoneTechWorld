package org.example.websitetechworld.Controller.AdminController.BaoHanhAdminController;

import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.LoaiBaoHanhAdminResponse;
import org.example.websitetechworld.Entity.LoaiBaoHanh;
import org.example.websitetechworld.Services.AdminServices.BaoHanhAdminServices.LoaiBaoHanhAdminServices;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/loai-bao-hanh")
public class LoaiBaoHanhAdminController {
    private final LoaiBaoHanhAdminServices loaiBaoHanhAdminServices;

    public LoaiBaoHanhAdminController(LoaiBaoHanhAdminServices loaiBaoHanhAdminServices) {
        this.loaiBaoHanhAdminServices = loaiBaoHanhAdminServices;
    }

    @GetMapping
    public ResponseEntity<Page<LoaiBaoHanhAdminResponse>> getAll (
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(loaiBaoHanhAdminServices.getAllLoaiBaoHanh(search, page, size));
    }
}
