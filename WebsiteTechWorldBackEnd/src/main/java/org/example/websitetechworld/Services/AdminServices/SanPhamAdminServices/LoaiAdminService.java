package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.LoaiAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.LoaiAdminResponse;
import org.example.websitetechworld.Entity.Loai;
import org.example.websitetechworld.Entity.ManHinh;
import org.example.websitetechworld.Repository.LoaiRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoaiAdminService {
    private final LoaiRepository loaiRepository;
    private final ModelMapper modelMapper;

    private LoaiAdminResponse convert(Loai loai) {
        return modelMapper.map(loai, LoaiAdminResponse.class);
    }

    private List<LoaiAdminResponse> convertList(List<Loai> loai) {
        return loai.stream()
                .map(this::convert)
                .toList();
    }

    public List<LoaiAdminResponse> getAllLoaiList() {
        List<Loai> loais = loaiRepository.findAll();
        return convertList(loais);
    }

    public Page<LoaiAdminResponse> getAllLoai(Pageable pageable) {
        Page<Loai> loais = loaiRepository.findAll(pageable);
        return loais.map(this::convert);
    }

    @Transactional
    public LoaiAdminResponse createLoai(LoaiAdminRequest loaiAdminRequest) {
        Loai loai = loaiRepository.save(modelMapper.map(loaiAdminRequest, Loai.class));
        return convert(loai);
    }

    @Transactional
    public LoaiAdminResponse updateLoai(Integer id, LoaiAdminRequest loaiAdminRequest) {
        Loai loai = loaiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy loại ID: " + id));
        modelMapper.map(loaiAdminRequest, loai);
        loaiRepository.save(loai);

        return convert(loai);
    }

    @Transactional
    public LoaiAdminResponse deleteLoai(Integer id) {
        Loai loai = loaiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy loại ID: " + id));
        loaiRepository.deleteById(id);

        return convert(loai);
    }

    public LoaiAdminResponse detailLoai(Integer id) {
        Loai loai = loaiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy loại ID: " + id));
        return convert(loai);
    }
}
