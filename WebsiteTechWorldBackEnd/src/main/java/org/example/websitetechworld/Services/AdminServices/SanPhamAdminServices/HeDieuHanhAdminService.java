package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HDHQuickCreateAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HeDieuHanhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HeDieuHanhAdminResponse;
import org.example.websitetechworld.Entity.HeDieuHanh;
import org.example.websitetechworld.Entity.ManHinh;
import org.example.websitetechworld.Repository.HeDieuHanhRepository;
import org.example.websitetechworld.exception.BusinessException;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.example.websitetechworld.exception.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HeDieuHanhAdminService {
    private final HeDieuHanhRepository heDieuHanhRepository;
    private final ModelMapper modelMapper;

    private HeDieuHanhAdminResponse convert(HeDieuHanh heDieuHanh) {
        return modelMapper.map(heDieuHanh, HeDieuHanhAdminResponse.class);
    }

    private List<HeDieuHanhAdminResponse> convertList(List<HeDieuHanh> heDieuHanh) {
        return heDieuHanh.stream()
                .map(this::convert)
                .toList();
    }

    public List<HeDieuHanhAdminResponse> getAllHeDieuHanhList() {
        List<HeDieuHanh> heDieuHanhs = heDieuHanhRepository.findAll();
        return convertList(heDieuHanhs);
    }

    //hiển thị màn hình phân trang
    public Page<HeDieuHanhAdminResponse> getAllHeDieuHanh(Pageable pageable) {
        Page<HeDieuHanh> heDieuHanhs = heDieuHanhRepository.findAll(pageable);
        return heDieuHanhs.map(this::convert);
    }

    //search, hiển thị, phân trang màn hình
    public Page<HeDieuHanhAdminResponse> getAllHeDieuHanh(Pageable pageable, String keyword) {
        Page<HeDieuHanh> pageResult;
        if(keyword !=null && !keyword.isEmpty()){
            pageResult = heDieuHanhRepository.findByKeyword(keyword, pageable);
        } else {
            pageResult = heDieuHanhRepository.findAll(pageable);
        }
        return pageResult.map(this:: convert);
    }


    @Transactional
    public HeDieuHanhAdminResponse createHeDieuHanh(HeDieuHanhAdminRequest heDieuHanhAdminRequest) {
        if (heDieuHanhRepository.existsByPhienBan(heDieuHanhAdminRequest.getPhienBan())) {
            throw new BusinessException("Phiên bản đã tồn tại");
        }

            HeDieuHanh heDieuHanh = heDieuHanhRepository.save(modelMapper.map(heDieuHanhAdminRequest, HeDieuHanh.class));
            return convert(heDieuHanh);
    }

    @Transactional
    public HeDieuHanhAdminResponse createHDHQuick (HDHQuickCreateAdminRequest request) {
        if (heDieuHanhRepository.existsByPhienBan(request.getPhienBan())) {
            throw new BusinessException("Phiên bản đã tồn tại");
        }
        HeDieuHanh dieuHanh = heDieuHanhRepository.save(modelMapper.map(request, HeDieuHanh.class));
        return convert(dieuHanh);
    }


    @Transactional
    public HeDieuHanhAdminResponse updateHeDieuHanh(Integer id, HeDieuHanhAdminRequest heDieuHanhAdminRequest) {
        HeDieuHanh dieuHanhOld = heDieuHanhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hệ điều hành ID: " + id));
        if (heDieuHanhRepository.existsByPhienBanAndIdNot(heDieuHanhAdminRequest.getPhienBan(), id)) {
            throw new BusinessException("Phiên bản đã tồn tại");
        }
            modelMapper.map(heDieuHanhAdminRequest, dieuHanhOld);
            heDieuHanhRepository.save(dieuHanhOld);
            return convert(dieuHanhOld);

    }

    @Transactional
    public HeDieuHanhAdminResponse deleteHeDieuHanh(Integer id) {
        HeDieuHanh dieuHanh = heDieuHanhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hệ điều hành ID: " + id));
        heDieuHanhRepository.deleteById(id);
        return convert(dieuHanh);
    }

    public HeDieuHanhAdminResponse detailHeDieuHanh(Integer id) {
        HeDieuHanh dieuHanh = heDieuHanhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hệ điều hành ID: " + id));
        return convert(dieuHanh);
    }
}
