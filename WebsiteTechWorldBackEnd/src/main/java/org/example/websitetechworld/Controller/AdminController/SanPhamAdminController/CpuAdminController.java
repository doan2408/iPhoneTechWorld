package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.Cpu;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.CpuAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cpu")
public class CpuAdminController {
    private final CpuAdminService cpuAdminService;

    @GetMapping
    public ResponseEntity<Page<Cpu>> getAllCpu(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<Cpu> cpus = cpuAdminService.getAllCpu(pageable);

        return ResponseEntity.ok(cpus);
    }
}
