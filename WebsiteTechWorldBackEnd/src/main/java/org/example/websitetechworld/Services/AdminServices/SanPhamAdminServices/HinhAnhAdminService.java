package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HinhAnhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HinhAnhAdminResponse;
import org.example.websitetechworld.Entity.HinhAnh;
import org.example.websitetechworld.Repository.HinhAnhRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HinhAnhAdminService {
    private final HinhAnhRepository hinhAnhRepository;

    private final ModelMapper modelMapper;

    public HinhAnhAdminResponse convertHinhAnh(HinhAnh hinhAnh) {
        return modelMapper.map(hinhAnh, HinhAnhAdminResponse.class);
    }

    public Page<HinhAnhAdminResponse> getAllHinhAnh(Pageable pageable) {

        Page<HinhAnh> hinhAnhs = hinhAnhRepository.findAll(pageable);

        return hinhAnhs.map(this::convertHinhAnh);
    }

    @Transactional
    public HinhAnhAdminResponse createHinhAnh(HinhAnhAdminRequest hinhAnhAdminRequest) {
        HinhAnh hinhAnh = hinhAnhRepository.save(modelMapper.map(hinhAnhAdminRequest, HinhAnh.class));
        return convertHinhAnh(hinhAnh);
    }

    @Transactional
    public HinhAnhAdminResponse updateHinhAnh(Integer id, HinhAnhAdminRequest hinhAnhAdminRequest) {
        HinhAnh hinhAnh = hinhAnhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không thấy hình ảnh ID: " + id));
        modelMapper.map(hinhAnhAdminRequest, hinhAnh);
        hinhAnhRepository.save(hinhAnh);

        return convertHinhAnh(hinhAnh);
    }

    @Transactional
    public HinhAnhAdminResponse deleteHinhAnh(Integer id) {
        HinhAnh hinhAnh = hinhAnhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không thấy hình ảnh ID: " + id));
        hinhAnhRepository.deleteById(id);

        return convertHinhAnh(hinhAnh);
    }

    public HinhAnhAdminResponse detailHinhAnh(Integer id) {
        HinhAnh hinhAnh = hinhAnhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không thấy hình ảnh ID: " + id));

        return convertHinhAnh(hinhAnh);
    }
}
