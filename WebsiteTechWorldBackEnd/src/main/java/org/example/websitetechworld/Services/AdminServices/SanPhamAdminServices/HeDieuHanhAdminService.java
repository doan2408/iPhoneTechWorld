package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HeDieuHanhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HeDieuHanhAdminResponse;
import org.example.websitetechworld.Entity.HeDieuHanh;
import org.example.websitetechworld.Repository.HeDieuHanhRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeDieuHanhAdminService {
    private final HeDieuHanhRepository heDieuHanhRepository;
    private final ModelMapper modelMapper;

    private HeDieuHanhAdminResponse convert(HeDieuHanh heDieuHanh) {
        return modelMapper.map(heDieuHanh, HeDieuHanhAdminResponse.class);
    }

    public Page<HeDieuHanhAdminResponse> getAllHeDieuHanh(Pageable pageable) {
        Page<HeDieuHanh> heDieuHanhs = heDieuHanhRepository.findAll(pageable);
        return heDieuHanhs.map(this::convert);
    }

    @Transactional
    public HeDieuHanhAdminResponse createHeDieuHanh(HeDieuHanhAdminRequest heDieuHanhAdminRequest) {
        HeDieuHanh dieuHanh = heDieuHanhRepository.save(modelMapper.map(heDieuHanhAdminRequest, HeDieuHanh.class));
        return convert(dieuHanh);
    }

    @Transactional
    public HeDieuHanhAdminResponse updateHeDieuHanh(Integer id, HeDieuHanhAdminRequest heDieuHanhAdminRequest) {
        HeDieuHanh dieuHanh = heDieuHanhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hệ điều hành ID: " + id));
        modelMapper.map(heDieuHanhAdminRequest, dieuHanh);
        heDieuHanhRepository.save(dieuHanh);

        return convert(dieuHanh);
    }

    @Transactional
    public HeDieuHanhAdminResponse deleteHeDieuHanh(Integer id) {
        HeDieuHanh dieuHanh = heDieuHanhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hệ điều hành ID: " + id));
        heDieuHanhRepository.deleteById(id);
        return convert(dieuHanh);
    }

    public HeDieuHanhAdminResponse detailHeDieuHanh(Integer id) {
        HeDieuHanh dieuHanh = heDieuHanhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hệ điều hành ID: " + id));
        return convert(dieuHanh);
    }
}
