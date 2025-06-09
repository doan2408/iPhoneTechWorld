package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CameraSauAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CameraTruocAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CameraSauAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CameraTruocAdminResponse;
import org.example.websitetechworld.Entity.CameraSau;
import org.example.websitetechworld.Entity.CameraTruoc;
import org.example.websitetechworld.Repository.CameraSauRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CameraSauAdminService {
    private final CameraSauRepository cameraSauRepository;
    private final ModelMapper modelMapper;

    private CameraSauAdminResponse convert(CameraSau cameraSau) {
        return modelMapper.map(cameraSau, CameraSauAdminResponse.class);
    }

    private List<CameraSauAdminResponse> convertList(List<CameraSau> cameraSau) {
        return cameraSau.stream()
                .map(this::convert)
                .toList();
    }

    public List<CameraSauAdminResponse> getAllCameraSauList() {
        List<CameraSau> cameraSaus = cameraSauRepository.findAll();
        return convertList(cameraSaus);
    }

    public Page<CameraSauAdminResponse> getAllCameraSau(Pageable pageable) {
        Page<CameraSau> cameraSaus = cameraSauRepository.findAll(pageable);
        return cameraSaus.map(this::convert);
    }

    @Transactional
    public CameraSauAdminResponse createCameraSau(CameraSauAdminRequest cameraSauAdminRequest) {
        CameraSau cameraSau = cameraSauRepository.save(modelMapper.map(cameraSauAdminRequest, CameraSau.class));
        return convert(cameraSau);
    }

    @Transactional
    public CameraSauAdminResponse updateCameraSau(Integer id, CameraSauAdminRequest cameraSauAdminRequest) {
        CameraSau cameraSau = cameraSauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Camera sau ID: " + id));
        modelMapper.map(cameraSauAdminRequest, cameraSau);
        cameraSauRepository.save(cameraSau);

        return convert(cameraSau);
    }

    @Transactional
    public CameraSauAdminResponse deleteCameraSau(Integer id) {
        CameraSau cameraSau = cameraSauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Camera sau ID: " + id));

        cameraSauRepository.deleteById(id);

        return convert(cameraSau);
    }

    public CameraSauAdminResponse detailCameraSau(Integer id) {
        CameraSau cameraSau = cameraSauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Camera sau ID: " + id));

        return convert(cameraSau);
    }
}
