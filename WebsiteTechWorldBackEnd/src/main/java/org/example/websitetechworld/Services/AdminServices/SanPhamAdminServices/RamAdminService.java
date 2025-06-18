package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.RamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.RamAdminResponse;
import org.example.websitetechworld.Entity.Ram;
import org.example.websitetechworld.Repository.RamRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RamAdminService {
    private final RamRepository ramRepository;
    private final ModelMapper modelMapper;

    private RamAdminResponse convert(Ram ram) {
        return modelMapper.map(ram, RamAdminResponse.class);
    }

    private List<RamAdminResponse> convertList(List<Ram> rams) {
        return rams.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public List<RamAdminResponse> getAllRamList() {
        List<Ram> rams = ramRepository.findAll();
        return convertList(rams);
    }

    public Page<RamAdminResponse> getAllRam(Pageable pageable) {
        Page<Ram> rams = ramRepository.findAll(pageable);
        return rams.map(this::convert);
    }

    public Page<RamAdminResponse> searchRams(String search, Pageable pageable) {
        Page<Ram> ramPage;
        if (StringUtils.hasText(search)) {
            ramPage = ramRepository.findByDungLuongContainingIgnoreCaseOrNhaSanXuatContainingIgnoreCase(search, search, pageable);
        }  else {
            ramPage = ramRepository.findAll(pageable);
        }
        return ramPage.map(this::convert);
    }

    @Transactional
    public RamAdminResponse createRam(RamAdminRequest ramAdminRequest) {
        if (ramRepository.existsByDungLuong(ramAdminRequest.getDungLuong())) {
            throw new IllegalArgumentException("Dung lượng đã tồn tại");
        }
        Ram ram = modelMapper.map(ramAdminRequest, Ram.class);
        Ram saved = ramRepository.save(ram);
        return convert(saved);
    }

    @Transactional
    public RamAdminResponse updateRam(Integer id, RamAdminRequest ramAdminRequest) {
        Ram ram = ramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy RAM ID: " + id));
        if (ramRepository.existsByDungLuongAndIdNot(ramAdminRequest.getDungLuong(), id)) {
            throw new IllegalArgumentException("Dung lượng đã tồn tại");
        }
        modelMapper.map(ramAdminRequest, ram);
        Ram saved = ramRepository.save(ram);
        return convert(saved);
    }

    @Transactional
    public RamAdminResponse deleteRam(Integer id) {
        Ram ram = ramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy RAM ID: " + id));
        ramRepository.deleteById(id);
        return convert(ram);
    }

    public RamAdminResponse detailRam(Integer id) {
        Ram ram = ramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy RAM ID: " + id));
        return convert(ram);
    }

    public boolean existsByDungLuong(String dungLuong) {
        return ramRepository.existsByDungLuong(dungLuong);
    }

    public boolean existsByDungLuongAndIdNot(String dungLuong, Integer id) {
        return ramRepository.existsByDungLuongAndIdNot(dungLuong, id);
    }
}