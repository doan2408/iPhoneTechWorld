package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.Cpu;
import org.example.websitetechworld.Repository.CpuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CpuAdminService {
    private final CpuRepository cpuRepository;

    public Page<Cpu> getAllCpu(Pageable pageable) {
        return cpuRepository.findAll(pageable);
    }
}
