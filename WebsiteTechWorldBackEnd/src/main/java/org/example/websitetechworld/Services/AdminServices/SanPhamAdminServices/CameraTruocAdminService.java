package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CameraTruocAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CameraTruocAdminResponse;
import org.example.websitetechworld.Entity.CameraTruoc;
import org.example.websitetechworld.Repository.CameraTruocRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CameraTruocAdminService {
    private final CameraTruocRepository cameraTruocRepository;
    private final ModelMapper modelMapper;

    private CameraTruocAdminResponse convert(CameraTruoc cameraTruoc) {
        return modelMapper.map(cameraTruoc, CameraTruocAdminResponse.class);
    }

    public Page<CameraTruocAdminResponse> getAllCameraTruoc(Pageable pageable) {
        Page<CameraTruoc> cameraTruocs = cameraTruocRepository.findAll(pageable);
        return cameraTruocs.map(this::convert);
    }

    @Transactional
    public CameraTruocAdminResponse createCameraTruoc(CameraTruocAdminRequest cameraTruocAdminRequest) {
        CameraTruoc cameraTruoc = cameraTruocRepository.save(modelMapper.map(cameraTruocAdminRequest, CameraTruoc.class));
        return convert(cameraTruoc);
    }

    @Transactional
    public CameraTruocAdminResponse updateCameraTruoc(Integer id, CameraTruocAdminRequest cameraTruocAdminRequest) {
        CameraTruoc cameraTruoc = cameraTruocRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Camera trước ID: " + id));
        modelMapper.map(cameraTruocAdminRequest, cameraTruoc);
        cameraTruocRepository.save(cameraTruoc);

        return convert(cameraTruoc);
    }

    @Transactional
    public CameraTruocAdminResponse deleteCameraTruoc(Integer id) {
        CameraTruoc cameraTruoc = cameraTruocRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Camera trước ID: " + id));

        cameraTruocRepository.deleteById(id);

        return convert(cameraTruoc);
    }

    public CameraTruocAdminResponse detailCameraTruoc(Integer id) {
        CameraTruoc cameraTruoc = cameraTruocRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Camera trước ID: " + id));

        return convert(cameraTruoc);
    }
}
