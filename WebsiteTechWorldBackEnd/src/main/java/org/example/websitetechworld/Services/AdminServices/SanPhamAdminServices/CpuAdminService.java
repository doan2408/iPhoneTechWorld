package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CpuAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CpuAdminResponse;
import org.example.websitetechworld.Entity.Cpu;
import org.example.websitetechworld.Repository.CpuRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.example.websitetechworld.exception.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CpuAdminService {
    private final CpuRepository cpuRepository;
    private final ModelMapper modelMapper;

    private CpuAdminResponse convert(Cpu cpu) {
        return modelMapper.map(cpu, CpuAdminResponse.class);
    }

    private List<CpuAdminResponse> convertList(List<Cpu> cpu) {
        return cpu.stream()
                .map(this::convert)
                .toList();
    }

    public List<CpuAdminResponse> getAllCpuList() {
        List<Cpu> cpus = cpuRepository.findAll();
        return convertList(cpus);
    }

    //hiển thị màn hình phân trang
    public Page<CpuAdminResponse> getAllCpu(Pageable pageable) {
        Page<Cpu> cpus = cpuRepository.findAll(pageable);
        return cpus.map(this::convert);
    }

    //search, hiển thị, phân trang màn hình
    public Page<CpuAdminResponse> getAllCpu(Pageable pageable, String keyword) {
        Page<Cpu> pageResult;
        if(keyword != null && !keyword.isEmpty()) {
            pageResult = cpuRepository.findByKeyword(keyword, pageable);
        }
        else {
            pageResult = cpuRepository.findAll(pageable);
        }
        return pageResult.map(this::convert);
    }


    @Transactional
    public CpuAdminResponse createCpu(CpuAdminRequest req) {
        List<Map<String, String>> errors = new ArrayList<>();

        Integer count = cpuRepository.countCheckTrung(
              req.getChipXuLy()
        );
        if(count > 0) {
            errors.add(Map.of("field", "cpu", "message", "Chip xử lý đã tồn tại"));
        }

        if(!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Cpu cpu = cpuRepository.save(modelMapper.map(req, Cpu.class));

        return convert(cpu);
    }

    @Transactional
    public CpuAdminResponse updateCpu(Integer id, CpuAdminRequest cpuAdminRequest) {
        Cpu cpuOld = cpuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Cpu ID: " + id));
        List<Map<String, String>> errors = new ArrayList<>();
        boolean unchanged =
                Objects.equals(cpuAdminRequest.getHangSanXuat(),cpuOld.getHangSanXuat()) &&
                        Objects.equals(cpuAdminRequest.getSoNhan(), cpuOld.getSoNhan()) &&
                        Objects.equals(cpuAdminRequest.getChipXuLy(), cpuOld.getChipXuLy()) &&
                        Objects.equals(cpuAdminRequest.getXungNhip(), cpuOld.getXungNhip()) &&
                        Objects.equals(cpuAdminRequest.getCongNgheSanXuat(), cpuOld.getCongNgheSanXuat()) &&
                        Objects.equals(cpuAdminRequest.getBoNhoDem(), cpuOld.getBoNhoDem()) &&
                        Objects.equals(cpuAdminRequest.getTieuThuDienNang(), cpuOld.getTieuThuDienNang());
        //không có thay đổi -> trả về dữ liệu cũ
        if(unchanged) {
            return convert(cpuOld);
        }
        else {
            Integer count = cpuRepository.countCheckTrung(
                    cpuAdminRequest.getChipXuLy()
            );
            if(count > 0) {
                errors.add(Map.of("field", "cpu", "message", "Chip xử lý đã tồn tại"));
            }
        }

        if(!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        modelMapper.map(cpuAdminRequest, cpuOld);
        cpuRepository.save(cpuOld);
        return convert(cpuOld);
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
