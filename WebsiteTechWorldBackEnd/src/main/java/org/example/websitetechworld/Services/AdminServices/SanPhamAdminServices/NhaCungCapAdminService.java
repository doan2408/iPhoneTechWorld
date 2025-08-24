package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.NhaCungCapAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.NhaCungCapQuickCreateAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.NhaCungCapAdminResponse;
import org.example.websitetechworld.Entity.NhaCungCap;
import org.example.websitetechworld.Repository.NhaCungCapRepository;
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
public class NhaCungCapAdminService {
    private final NhaCungCapRepository nhaCungCapRepository;
    private final ModelMapper modelMapper;

    private NhaCungCapAdminResponse convert(NhaCungCap nhaCungCap) {
        return modelMapper.map(nhaCungCap, NhaCungCapAdminResponse.class);
    }

    private List<NhaCungCapAdminResponse> convertList(List<NhaCungCap> nhaCungCaps) {
        return nhaCungCaps.stream()
                .map(this::convert)
                .toList();
    }

    public Page<NhaCungCapAdminResponse> getAllNhaCungCap(Pageable pageable) {
        Page<NhaCungCap> nhaCungCaps = nhaCungCapRepository.findAll(pageable);
        return nhaCungCaps.map(this::convert);
    }

    public List<NhaCungCapAdminResponse> getAllNhaCungCapList() {
        List<NhaCungCap> nhaCungCaps = nhaCungCapRepository.findAll();
        return convertList(nhaCungCaps);
    }

    public Page<NhaCungCapAdminResponse> searchNhaCungCap(String search, Pageable pageable) {
        Page<NhaCungCap> nhaCungCapPage;
        if (StringUtils.hasText(search)) {
            nhaCungCapPage = nhaCungCapRepository.findByTenNhaCungCapContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
        }  else {
            nhaCungCapPage = nhaCungCapRepository.findAll(pageable);
        }
        return nhaCungCapPage.map(this::convert);
    }

    @Transactional
    public NhaCungCapAdminResponse createNCC(NhaCungCapAdminRequest nhaCungCapAdminRequest) {

        if (!nhaCungCapAdminRequest.getSdt().trim().matches("^(03|05|07|08|09)\\d{8}$")) {
            throw new BusinessException("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 03, 05, 07, 08 hoặc 09 (ví dụ: 0987654321)");
        }

        nhaCungCapAdminRequest.setSdt(nhaCungCapAdminRequest.getSdt().trim());

        if (nhaCungCapRepository.existsBySdt(nhaCungCapAdminRequest.getSdt().trim())) {
            throw new BusinessException("Số điện thoại này đã tồn tại trong hệ thống");
        }

        if (nhaCungCapRepository.existsByEmail(nhaCungCapAdminRequest.getEmail().trim())) {
            throw new BusinessException("Email này đã tồn tại trong hệ thống");
        }

        if (nhaCungCapRepository.existsByTenNhaCungCap(nhaCungCapAdminRequest.getTenNhaCungCap())) {
            throw new BusinessException("Tên nhà cung cấp đã tồn tại");
        }


        NhaCungCap cungCap = modelMapper.map(nhaCungCapAdminRequest, NhaCungCap.class);
        NhaCungCap saved = nhaCungCapRepository.save(cungCap);
        return convert(saved);
    }

    @Transactional
    public NhaCungCapAdminResponse quickCreateNCC(NhaCungCapQuickCreateAdminRequest req) {
        if (nhaCungCapRepository.existsByTenNhaCungCap(req.getTenNhaCungCap())) {
            throw new BusinessException("Tên nhà cung cấp đã tồn tại");
        }
        NhaCungCap cungCap = modelMapper.map(req, NhaCungCap.class);
        NhaCungCap saved = nhaCungCapRepository.save(cungCap);
        return convert(saved);
    }

    @Transactional
    public NhaCungCapAdminResponse updateNCC(Integer id, NhaCungCapAdminRequest request) {
        // 1. Tìm NCC theo id
        NhaCungCap ncc = nhaCungCapRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhà cung cấp ID: " + id));

        String sdt = request.getSdt() != null ? request.getSdt().trim() : null;
        String email = request.getEmail() != null ? request.getEmail().trim() : null;
        String tenNCC = request.getTenNhaCungCap() != null ? request.getTenNhaCungCap().trim() : null;

        if (sdt != null && !sdt.matches("^(03|05|07|08|09)\\d{8}$")) {
            throw new BusinessException("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 03, 05, 07, 08 hoặc 09 (ví dụ: 0987654321)");
        }

        if (sdt != null && nhaCungCapRepository.existsBySdtAndIdNot(sdt, id)) {
            throw new BusinessException("Số điện thoại này đã tồn tại trong hệ thống");
        }

        if (email != null && nhaCungCapRepository.existsByEmailAndIdNot(email, id)) {
            throw new BusinessException("Email này đã tồn tại trong hệ thống");
        }

        if (tenNCC != null && nhaCungCapRepository.existsByTenNhaCungCapAndIdNot(tenNCC, id)) {
            throw new BusinessException("Tên nhà cung cấp đã tồn tại");
        }

        ncc.setTenNhaCungCap(tenNCC);
        ncc.setSdt(sdt);
        ncc.setEmail(email);
        ncc.setDiaChi(request.getDiaChi() != null ? request.getDiaChi().trim() : null);

        NhaCungCap saved = nhaCungCapRepository.save(ncc);

        return convert(saved);
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
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhà cung cấp ID: " + id));
        return convert(cungCap);
    }

}