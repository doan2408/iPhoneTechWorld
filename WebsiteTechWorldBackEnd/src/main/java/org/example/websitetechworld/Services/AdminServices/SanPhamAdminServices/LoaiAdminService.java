package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.LoaiAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.LoaiAdminResponse;
import org.example.websitetechworld.Entity.Loai;
import org.example.websitetechworld.Repository.LoaiRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoaiAdminService {
    private final LoaiRepository loaiRepository;
    private final ModelMapper modelMapper;

    private LoaiAdminResponse convert(Loai loai) {
        return modelMapper.map(loai, LoaiAdminResponse.class);
    }

    private List<LoaiAdminResponse> convertList(List<Loai> loais) {
        return loais.stream()
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

    public Page<LoaiAdminResponse> searchLoai(String tenLoai, Pageable pageable) {
        Page<Loai> loaiPage;
        if (StringUtils.hasText(tenLoai)) {
            loaiPage = loaiRepository.findByTenLoaiContainingIgnoreCase(tenLoai, pageable);
        } else {
            loaiPage = loaiRepository.findAll(pageable);
        }
        return loaiPage.map(this::convert);
    }

    @Transactional
    public LoaiAdminResponse createLoai(LoaiAdminRequest loaiAdminRequest) {
        if (loaiRepository.existsByTenLoai(loaiAdminRequest.getTenLoai())) {
            throw new IllegalArgumentException("Tên loại đã tồn tại");
        }
        Loai loai = modelMapper.map(loaiAdminRequest, Loai.class);
        Loai saved = loaiRepository.save(loai);
        return convert(saved);
    }

    @Transactional
    public LoaiAdminResponse updateLoai(Integer id, LoaiAdminRequest loaiAdminRequest) {
        Loai loai = loaiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy loại ID: " + id));
        if (loaiRepository.existsByTenLoaiAndIdNot(loaiAdminRequest.getTenLoai(), id)) {
            throw new IllegalArgumentException("Tên loại đã tồn tại");
        }
        modelMapper.map(loaiAdminRequest, loai);
        Loai saved = loaiRepository.save(loai);
        return convert(saved);
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

    public boolean existsByTenLoai(String tenLoai) {
        return loaiRepository.existsByTenLoai(tenLoai);
    }

    public boolean existsByTenLoaiAndIdNot(String tenLoai, Integer id) {
        return loaiRepository.existsByTenLoaiAndIdNot(tenLoai, id);
    }
}