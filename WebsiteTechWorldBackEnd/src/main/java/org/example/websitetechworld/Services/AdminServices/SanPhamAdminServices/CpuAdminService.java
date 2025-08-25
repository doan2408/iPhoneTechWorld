package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CpuAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CpuQuickCreateAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CpuAdminResponse;
import org.example.websitetechworld.Entity.Cpu;
import org.example.websitetechworld.Repository.CpuRepository;
import org.example.websitetechworld.exception.BusinessException;
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
    public CpuAdminResponse createCpu(CpuAdminRequest cpuAdminRequest) {

        validateCpu(cpuAdminRequest);

        if (cpuRepository.existsByChipXuLy(cpuAdminRequest.getChipXuLy())) {
            throw new BusinessException("Chíp xử lý đã tồn tại");
        }

        Cpu cpu = cpuRepository.save(modelMapper.map(cpuAdminRequest, Cpu.class));

        return convert(cpu);
    }

    @Transactional
    public CpuAdminResponse createCpuQuick (CpuQuickCreateAdminRequest request) {

        String chip = request.getChipXuLy().trim().replaceAll("\\s+", " ");
        if (!chip.matches("^[a-zA-Z0-9 ]+$")) { // chỉ còn space đơn
            throw new BusinessException("Chíp xử lý chỉ được chứa chữ, số, không chứa ký tự đặc biệt");
        }

        if (cpuRepository.existsByChipXuLy(request.getChipXuLy())) {
            throw new BusinessException("Chíp xử lý đã tồn tại");
        }

        Cpu cpu = cpuRepository.save(modelMapper.map(request, Cpu.class));

        return convert(cpu);
    }


    // đang sửa ở đây
    @Transactional
    public CpuAdminResponse updateCpu(Integer id, CpuAdminRequest cpuAdminRequest) {
        Cpu cpuOld = cpuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Cpu ID: " + id));

        validateCpu(cpuAdminRequest);

        if (cpuRepository.existsByChipXuLyAndIdNot(
                cpuAdminRequest.getChipXuLy(),
                id
        )) {
            throw new BusinessException("Chíp xử lý đã tồn tại");
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


    public void validateCpu(CpuAdminRequest cpuAdminRequest) {
        // Lấy và trim chip xử lý
        String chip = cpuAdminRequest.getChipXuLy().trim();
        if (!chip.matches("^Apple[\\p{L}\\d ]*$")) {
            throw new BusinessException("Chip xử lý phải bắt đầu bằng 'Apple' và chỉ được chứa chữ, số, khoảng trắng (ví dụ: Apple M1, Apple A16 Bionic)");
        }
        cpuAdminRequest.setChipXuLy(chip.trim());

        String soNhan = cpuAdminRequest.getSoNhan().trim();
        if (!soNhan.matches("^[\\p{L}\\d ]+$")) {
            throw new BusinessException("Số nhân chỉ được chứa chữ, số và khoảng trắng, không được chứa ký tự đặc biệt");
        }
        cpuAdminRequest.setSoNhan(soNhan.trim());

        String xungNhip = cpuAdminRequest.getXungNhip().trim();
        if (!xungNhip.matches("^[\\p{L}\\d ]+ GHz$")) {
            throw new BusinessException("Xung nhịp chỉ được chứa chữ, số, khoảng trắng và phải kết thúc bằng 'GHz' (ví dụ: 3.5 GHz)");
        }
        cpuAdminRequest.setXungNhip(xungNhip);

        String CNSX = cpuAdminRequest.getCongNgheSanXuat().trim();
        if (!CNSX.matches("^[\\p{L}\\d ]+ nm$")) {
            throw new BusinessException("Công nghệ sản xuất chỉ được chứa chữ, số, khoảng trắng và phải kết thúc bằng 'nm' (ví dụ: 7 nm, 5 nm)");
        }
        cpuAdminRequest.setCongNgheSanXuat(CNSX);

        String boNhoDem = cpuAdminRequest.getBoNhoDem().trim();
        if (!boNhoDem.matches("^[\\p{L}\\d ]+ MB$")) {
            throw new BusinessException("Bộ nhớ đệm chỉ được chứa chữ, số, khoảng trắng và phải kết thúc bằng 'MB' (ví dụ: 8 MB, 12 MB)");
        }
        cpuAdminRequest.setBoNhoDem(boNhoDem);

        String tieuThuDienNang = cpuAdminRequest.getTieuThuDienNang().trim();
        if (!tieuThuDienNang.matches("^[\\p{L}\\d ]+ W$")) {
            throw new BusinessException("Tiêu thụ điện năng chỉ được chứa chữ, số, khoảng trắng và phải kết thúc bằng 'W' (ví dụ: 65 W, 125 W)");
        }
        cpuAdminRequest.setTieuThuDienNang(tieuThuDienNang);


    }


}
