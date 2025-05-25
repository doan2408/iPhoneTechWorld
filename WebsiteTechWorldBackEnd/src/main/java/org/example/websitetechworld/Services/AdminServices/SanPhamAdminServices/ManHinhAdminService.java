package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ManHinhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ManHinhAdminResponse;
import org.example.websitetechworld.Entity.ManHinh;
import org.example.websitetechworld.Entity.MauSac;
import org.example.websitetechworld.Repository.ManHinhRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManHinhAdminService {
    private final ManHinhRepository manHinhRepository;
    private final ModelMapper modelMapper;

    private ManHinhAdminResponse convert(ManHinh manHinh) {
        return modelMapper.map(manHinh, ManHinhAdminResponse.class);
    }

    public Page<ManHinhAdminResponse> getAllManHinh(Pageable pageable) {
        Page<ManHinh> manHinhs = manHinhRepository.findAll(pageable);
        return manHinhs.map(this::convert);
    }

    @Transactional
    public ManHinhAdminResponse createManHinh(ManHinhAdminRequest manHinhAdminRequest) {
        ManHinh manHinh = manHinhRepository.save(modelMapper.map(manHinhAdminRequest, ManHinh.class));
        return convert(manHinh);
    }

    @Transactional
    public ManHinhAdminResponse updateManHinh(Integer id, ManHinhAdminRequest manHinhAdminRequest) {
        ManHinh manHinh = manHinhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màn hình ID: " + id));
        modelMapper.map(manHinhAdminRequest, manHinh);
        manHinhRepository.save(manHinh);

        return convert(manHinh);
    }

    @Transactional
    public ManHinhAdminResponse deleteManHinh(Integer id) {
        ManHinh manHinh = manHinhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màn hình ID: " + id));
        manHinhRepository.deleteById(id);

        return convert(manHinh);
    }

    public ManHinhAdminResponse detailManHinh(Integer id) {
        ManHinh manHinh = manHinhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màn hình ID: " + id));

        return convert(manHinh);
    }
}
