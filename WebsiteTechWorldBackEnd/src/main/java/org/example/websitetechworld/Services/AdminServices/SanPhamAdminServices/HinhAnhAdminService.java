package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.HinhAnhAdminResponse;
import org.example.websitetechworld.Entity.HinhAnh;
import org.example.websitetechworld.Repository.HinhAnhRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}
