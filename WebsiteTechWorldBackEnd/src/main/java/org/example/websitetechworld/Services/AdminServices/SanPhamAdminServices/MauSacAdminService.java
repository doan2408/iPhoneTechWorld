package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.MauSacAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.MauSacQuickAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.MauSacAdminResponse;
import org.example.websitetechworld.Entity.MauSac;
import org.example.websitetechworld.Repository.MauSacRepository;
import org.example.websitetechworld.exception.BusinessException;
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
        if (!mauSacAdminRequest.getTenMau().trim().matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
            throw new BusinessException("Tên màu không được chứa ký tự đặc biệt hoặc số. Ví dụ: Đỏ, Xanh lá, Vàng.");
        }

        if (mauSacRepository.existsByTenMau(mauSacAdminRequest.getTenMau())) {
            throw new BusinessException("Tên màu đã tồn tại");
        }

        mauSacAdminRequest.setTenMau(mauSacAdminRequest.getTenMau().trim());

        MauSac mauSac = modelMapper.map(mauSacAdminRequest, MauSac.class);
        MauSac saved = mauSacRepository.save(mauSac);
        return convert(saved);
    }

    @Transactional
    public MauSacAdminResponse createMauSacQuick(MauSacQuickAdminRequest mauSacAdminRequest) {
        if (!mauSacAdminRequest.getTenMau().trim().matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
            throw new BusinessException("Tên màu không được chứa ký tự đặc biệt hoặc số. Ví dụ: Đỏ, Xanh lá, Vàng.");
        }
        if (mauSacRepository.existsByTenMau(mauSacAdminRequest.getTenMau())) {
            throw new BusinessException("Tên màu đã tồn tại");
        }
        mauSacAdminRequest.setTenMau(mauSacAdminRequest.getTenMau().trim());
        MauSac mauSac = modelMapper.map(mauSacAdminRequest, MauSac.class);
        MauSac saved = mauSacRepository.save(mauSac);
        return convert(saved);
    }



    @Transactional
    public MauSacAdminResponse updateMauSac(Integer id, MauSacAdminRequest mauSacAdminRequest) {
        MauSac mauSac = mauSacRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màu sắc ID: " + id));

        if (!mauSacAdminRequest.getTenMau().trim().matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
            throw new BusinessException("Tên màu không được chứa ký tự đặc biệt hoặc số. Ví dụ: Đỏ, Xanh lá, Vàng.");
        }

        if (mauSacRepository.existsByTenMauAndIdNot(mauSacAdminRequest.getTenMau(), mauSac.getId())) {
            throw new BusinessException("Tên màu đã tồn tại");
        }
        mauSacAdminRequest.setTenMau(mauSacAdminRequest.getTenMau().trim());

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

}