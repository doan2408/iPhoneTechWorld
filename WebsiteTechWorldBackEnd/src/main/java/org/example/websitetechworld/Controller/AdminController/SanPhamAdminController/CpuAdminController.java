package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CpuAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HeDieuHanhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CpuAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HeDieuHanhAdminResponse;
import org.example.websitetechworld.Entity.Cpu;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.CpuAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cpu")
public class CpuAdminController {
    private final CpuAdminService cpuAdminService;

    @GetMapping
    public ResponseEntity<Page<CpuAdminResponse>> getAllCpu(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<CpuAdminResponse> cpus = cpuAdminService.getAllCpu(pageable);

        return ResponseEntity.ok(cpus);
    }

    @GetMapping("/listCpu")
    public ResponseEntity<List<CpuAdminResponse>> getAllCpuList() {
        List<CpuAdminResponse> cpus = cpuAdminService.getAllCpuList();
        return ResponseEntity.ok(cpus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CpuAdminResponse> detailCpu(@PathVariable Integer id) {
        CpuAdminResponse response = cpuAdminService.detailCpu(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CpuAdminResponse> createCpu(@RequestBody CpuAdminRequest cpuAdminRequest) {
        CpuAdminResponse response = cpuAdminService.createCpu(cpuAdminRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CpuAdminResponse> updateCpu(@PathVariable Integer id, @RequestBody CpuAdminRequest cpuAdminRequest) {
        CpuAdminResponse response = cpuAdminService.updateCpu(id, cpuAdminRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CpuAdminResponse> deleteCpu(@PathVariable Integer id) {
        CpuAdminResponse response = cpuAdminService.deleteCpu(id);

        return ResponseEntity.ok(response);
    }
}
