package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CpuAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CpuQuickCreateAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HeDieuHanhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CpuAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HeDieuHanhAdminResponse;
import org.example.websitetechworld.Entity.Cpu;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.CpuAdminService;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cpu")
public class CpuAdminController {
    private final CpuAdminService cpuAdminService;

    @GetMapping
    public ResponseEntity<Page<CpuAdminResponse>> getAllCpu(
            @PageableDefault(page = 0, size = 5) Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        Page<CpuAdminResponse> cpus = cpuAdminService.getAllCpu(pageable, keyword);

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
    public ResponseEntity<CpuAdminResponse> createCpu(@Valid @RequestBody CpuAdminRequest cpuAdminRequest) {
        CpuAdminResponse response = cpuAdminService.createCpu(cpuAdminRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/quick-cpu")
    public ResponseEntity<CpuAdminResponse> createCpuQuick(@RequestBody @Valid CpuQuickCreateAdminRequest cpuAdminRequest) {
            CpuAdminResponse response = cpuAdminService.createCpuQuick(cpuAdminRequest);
            return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCpu( @PathVariable Integer id, @Valid @RequestBody CpuAdminRequest cpuAdminRequest) {
         CpuAdminResponse response = cpuAdminService.updateCpu(id, cpuAdminRequest);
         return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CpuAdminResponse> deleteCpu(@PathVariable Integer id) {
        CpuAdminResponse response = cpuAdminService.deleteCpu(id);

        return ResponseEntity.ok(response);
    }
}
