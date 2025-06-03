package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CameraTruocAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CpuAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CameraTruocAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CpuAdminResponse;
import org.example.websitetechworld.Entity.CameraTruoc;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.CameraTruocAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cameraTruoc")
public class CameraTruocAdminController {
    private final CameraTruocAdminService cameraTruocAdminService;

    @GetMapping
    public ResponseEntity<Page<CameraTruocAdminResponse>> getAllCameraTruoc(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<CameraTruocAdminResponse> cameraTruocs = cameraTruocAdminService.getAllCameraTruoc(pageable);
        return ResponseEntity.ok(cameraTruocs);
    }

    @GetMapping("/listCameraTruoc")
    public ResponseEntity<List<CameraTruocAdminResponse>> getAllCameraTruocList() {
        List<CameraTruocAdminResponse> cameraTruocs = cameraTruocAdminService.getAllCameraTruocList();
        return ResponseEntity.ok(cameraTruocs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CameraTruocAdminResponse> detailCameraTruoc(@PathVariable Integer id) {
        CameraTruocAdminResponse response = cameraTruocAdminService.detailCameraTruoc(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CameraTruocAdminResponse> createCameraTruoc(@RequestBody CameraTruocAdminRequest cameraTruocAdminRequest) {
        CameraTruocAdminResponse response = cameraTruocAdminService.createCameraTruoc(cameraTruocAdminRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CameraTruocAdminResponse> updateCameraTruoc(@PathVariable Integer id, @RequestBody CameraTruocAdminRequest cameraTruocAdminRequest) {
        CameraTruocAdminResponse response = cameraTruocAdminService.updateCameraTruoc(id, cameraTruocAdminRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CameraTruocAdminResponse> deleteCameraTruoc(@PathVariable Integer id) {
        CameraTruocAdminResponse response = cameraTruocAdminService.deleteCameraTruoc(id);

        return ResponseEntity.ok(response);
    }
}
