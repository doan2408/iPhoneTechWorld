package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.NhaCungCapAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.NhaCungCapAdminResponse;
import org.example.websitetechworld.Entity.NhaCungCap;
import org.example.websitetechworld.Entity.Pin;
import org.example.websitetechworld.Repository.NhaCungCapRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NhaCungCapAdminService {
    private final NhaCungCapRepository nhaCungCapRepository;
    private final ModelMapper modelMapper;

    private NhaCungCapAdminResponse convert(NhaCungCap nhaCungCap) {
        return modelMapper.map(nhaCungCap, NhaCungCapAdminResponse.class);
    }

    public Page<NhaCungCapAdminResponse> getAllNhaCungCap(Pageable pageable) {
        Page<NhaCungCap> nhaCungCaps = nhaCungCapRepository.findAll(pageable);
        return nhaCungCaps.map(this::convert);
    }

    @Transactional
    public NhaCungCapAdminResponse createNCC(NhaCungCapAdminRequest nhaCungCapAdminRequest) {
        NhaCungCap cungCap = nhaCungCapRepository.save(modelMapper.map(nhaCungCapAdminRequest, NhaCungCap.class));
        return convert(cungCap);
    }

    @Transactional
    public NhaCungCapAdminResponse updateNCC(Integer id, NhaCungCapAdminRequest NhaCungCapAdminRequest) {
        NhaCungCap cungCap = nhaCungCapRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhà cung cấp ID: " + id));
        modelMapper.map(NhaCungCapAdminRequest, cungCap);
        nhaCungCapRepository.save(cungCap);
        return convert(cungCap);
    }

    @Transactional
    public NhaCungCapAdminResponse deleteNCC(Integer id) {
        NhaCungCap nhaCungCap = nhaCungCapRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhà cung cấp ID: " + id));
        nhaCungCapRepository.deleteById(id);

        return convert(nhaCungCap);
    }

    public NhaCungCapAdminResponse detailNCC(Integer id) {
        NhaCungCap cungCap = nhaCungCapRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhà cung cấp ID:" + id));
        return convert(cungCap);
    }
}
