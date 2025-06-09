package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.MauSacAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.MauSacAdminResponse;
import org.example.websitetechworld.Entity.MauSac;
import org.example.websitetechworld.Entity.NhaCungCap;
import org.example.websitetechworld.Repository.MauSacRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MauSacAdminService {
    private final MauSacRepository mauSacRepository;
    private final ModelMapper modelMapper;

    private MauSacAdminResponse convert(MauSac mauSac) {
        return modelMapper.map(mauSac, MauSacAdminResponse.class);
    }

    private List<MauSacAdminResponse> convertList(List<MauSac> mauSac) {
        return mauSac.stream()
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

    @Transactional
    public MauSacAdminResponse createMauSac(MauSacAdminRequest mauSacAdminRequest) {
        MauSac mauSac = mauSacRepository.save(modelMapper.map(mauSacAdminRequest, MauSac.class));
        return convert(mauSac);
    }

    @Transactional
    public MauSacAdminResponse updateMauSac(Integer id, MauSacAdminRequest mauSacAdminRequest) {
        MauSac mauSac = mauSacRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màu sắc ID: " + id));
        modelMapper.map(mauSacAdminRequest, mauSac);
        mauSacRepository.save(mauSac);

        return convert(mauSac);
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
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màu sắc ID:" + id));
        return convert(mauSac);
    }
}
