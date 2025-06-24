package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.MauSacAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.MauSacAdminResponse;
import org.example.websitetechworld.Entity.MauSac;
import org.example.websitetechworld.Repository.MauSacRepository;
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
public class MauSacAdminService {
    private final MauSacRepository mauSacRepository;
    private final ModelMapper modelMapper;

    private MauSacAdminResponse convert(MauSac mauSac) {
        return modelMapper.map(mauSac, MauSacAdminResponse.class);
    }

    private List<MauSacAdminResponse> convertList(List<MauSac> mauSacs) {
        return mauSacs.stream()
                .map(this::convert)
                .toList();
    }

    public List<MauSacAdminResponse> getAllMauSacList() {
        List<MauSac> mauSacs = mauSacRepository.findAll();
        return convertList(mauSacs);
    }

    public Page<MauSacAdminResponse> getAllMauSac(Pageable pageable) {
        Page<MauSac> mauSacs = mauSacRepository.findAll(pageable);
        return mauSacs.map(this::convert);
    }

    public Page<MauSacAdminResponse> searchMauSac(String tenMau, Pageable pageable) {
        Page<MauSac> mauSacPage;
        if (StringUtils.hasText(tenMau)) {
            mauSacPage = mauSacRepository.findByTenMauContainingIgnoreCase(tenMau, pageable);
        } else {
            mauSacPage = mauSacRepository.findAll(pageable);
        }
        return mauSacPage.map(this::convert);
    }

    @Transactional
    public MauSacAdminResponse createMauSac(MauSacAdminRequest mauSacAdminRequest) {
        if (mauSacRepository.existsByTenMau(mauSacAdminRequest.getTenMau())) {
            throw new IllegalArgumentException("Tên màu đã tồn tại");
        }
        MauSac mauSac = modelMapper.map(mauSacAdminRequest, MauSac.class);
        MauSac saved = mauSacRepository.save(mauSac);
        return convert(saved);
    }

    @Transactional
    public MauSacAdminResponse updateMauSac(Integer id, MauSacAdminRequest mauSacAdminRequest) {
        MauSac mauSac = mauSacRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màu sắc ID: " + id));
        if (mauSacRepository.existsByTenMauAndIdNot(mauSacAdminRequest.getTenMau(), id)) {
            throw new IllegalArgumentException("Tên màu đã tồn tại");
        }
        modelMapper.map(mauSacAdminRequest, mauSac);
        MauSac saved = mauSacRepository.save(mauSac);
        return convert(saved);
    }

    @Transactional
    public MauSacAdminResponse deleteMauSac(Integer id) {
        MauSac mauSac = mauSacRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màu sắc ID: " + id));
        mauSacRepository.deleteById(id);
        return convert(mauSac);
    }

    public MauSacAdminResponse detailMauSac(Integer id) {
        MauSac mauSac = mauSacRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màu sắc ID: " + id));
        return convert(mauSac);
    }

    public boolean existsByTenMau(String tenMau) {
        return mauSacRepository.existsByTenMau(tenMau);
    }

    public boolean existsByTenMauAndIdNot(String tenMau, Integer id) {
        return mauSacRepository.existsByTenMauAndIdNot(tenMau, id);
    }
}