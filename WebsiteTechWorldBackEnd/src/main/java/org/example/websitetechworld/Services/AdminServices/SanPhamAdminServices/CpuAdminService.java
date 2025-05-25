package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CpuAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CpuAdminResponse;
import org.example.websitetechworld.Entity.Cpu;
import org.example.websitetechworld.Repository.CpuRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CpuAdminService {
    private final CpuRepository cpuRepository;
    private final ModelMapper modelMapper;

    private CpuAdminResponse convert(Cpu cpu) {
        return modelMapper.map(cpu, CpuAdminResponse.class);
    }

    public Page<CpuAdminResponse> getAllCpu(Pageable pageable) {
        Page<Cpu> cpus = cpuRepository.findAll(pageable);
        return cpus.map(this::convert);
    }

    @Transactional
    public CpuAdminResponse createCpu(CpuAdminRequest cpuAdminRequest) {
        Cpu cpu = cpuRepository.save(modelMapper.map(cpuAdminRequest, Cpu.class));

        return convert(cpu);
    }

    @Transactional
    public CpuAdminResponse updateCpu(Integer id, CpuAdminRequest cpuAdminRequest) {
        Cpu cpu = cpuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Cpu ID: " + id));
        modelMapper.map(cpuAdminRequest, cpu);
        cpuRepository.save(cpu);

        return convert(cpu);
    }

    @Transactional
    public CpuAdminResponse deleteCpu(Integer id) {
        Cpu cpu = cpuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Cpu ID: " + id));

        cpuRepository.deleteById(id);

        return convert(cpu);
    }

     public CpuAdminResponse detailCpu(Integer id) {
        Cpu cpu = cpuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Cpu ID: " + id));

        return convert(cpu);
    }


}
