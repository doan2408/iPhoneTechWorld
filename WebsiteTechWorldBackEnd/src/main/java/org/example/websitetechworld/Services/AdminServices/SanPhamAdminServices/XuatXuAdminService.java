package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.XuatXuAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.XuatXuQuickCreateAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.XuatXuAdminResponse;
import org.example.websitetechworld.Entity.XuatXu;
import org.example.websitetechworld.Repository.XuatXuRepository;
import org.example.websitetechworld.exception.BusinessException;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class XuatXuAdminService {
    private final XuatXuRepository xuatXuRepo;
    private final ModelMapper modelMapper;

    private XuatXuAdminResponse convert(XuatXu xuatXu) {
        return modelMapper.map(xuatXu, XuatXuAdminResponse.class);
    }

    private List<XuatXuAdminResponse> convertList(List<XuatXu> xuatXus) {
        return xuatXus.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public Page<XuatXuAdminResponse> getAllXuatXu(Pageable pageable) {
        Page<XuatXu> xuatXuPage = xuatXuRepo.findAll(pageable);
        return xuatXuPage.map(this::convert);
    }

    public List<XuatXuAdminResponse> getAllXuatXuList() {
        List<XuatXu> xuatXus = xuatXuRepo.findAll();
        return convertList(xuatXus);
    }

    public Page<XuatXuAdminResponse> searchXuatXu(String search, Pageable pageable) {
        Page<XuatXu> xuatXuPage;
        if (StringUtils.hasText(search)) {
            xuatXuPage = xuatXuRepo.findByMaXuatXuContainingIgnoreCaseOrTenQuocGiaContainingIgnoreCase(search, search, pageable);
        } else {
            xuatXuPage = xuatXuRepo.findAll(pageable);
        }
        return xuatXuPage.map(this::convert);
    }

    @Transactional
    public XuatXuAdminResponse createXuatXu(@Valid XuatXuAdminRequest xuatXuAdminRequest) {

        if (!xuatXuAdminRequest.getMaXuatXu().trim().matches("^[A-Z]+$")) {
            throw new BusinessException("Mã xuất xứ không hợp lệ! Chỉ được phép chứa chữ in hoa (A-Z), không có số, chữ thường hoặc ký tự đặc biệt. Ví dụ hợp lệ:US, VNA");
        }

        if (!xuatXuAdminRequest.getTenQuocGia().trim().matches("^[\\p{L}\\s]+$")) {
            throw new BusinessException(
                    "Tên quốc gia không hợp lệ! Chỉ được chứa chữ cái và khoảng trắng. Ví dụ hợp lệ: 'Việt Nam', 'Hoa Kỳ', 'Nhật Bản'"
            );
        }

        if (xuatXuRepo.existsByMaXuatXu(xuatXuAdminRequest.getMaXuatXu())) {
            throw new BusinessException("Mã xuất xứ đã tồn tại");
        }

        xuatXuAdminRequest.setMaXuatXu(xuatXuAdminRequest.getMaXuatXu().trim());
        xuatXuAdminRequest.setTenQuocGia(xuatXuAdminRequest.getTenQuocGia().trim());

        XuatXu xuatXu = modelMapper.map(xuatXuAdminRequest, XuatXu.class);
        XuatXu saved = xuatXuRepo.save(xuatXu);
        return modelMapper.map(saved, XuatXuAdminResponse.class);
    }

    @Transactional
    public XuatXuAdminResponse createQuickXuatXu(XuatXuQuickCreateAdminRequest req) {
        if (!req.getMaXuatXu().matches("^[A-Z]+$")) {
            throw new BusinessException("Mã xuất xứ không hợp lệ! Chỉ được phép chứa chữ in hoa (A-Z), không có số, chữ thường hoặc ký tự đặc biệt. Ví dụ hợp lệ:US, VNA");
        }

        if (xuatXuRepo.existsByMaXuatXu(req.getMaXuatXu())) {
            throw new BusinessException("Mã xuất xứ đã tồn tại");
        }

        req.setMaXuatXu(req.getMaXuatXu().trim());

        XuatXu xuatXu = modelMapper.map(req, XuatXu.class);
        xuatXuRepo.save(xuatXu);

        return modelMapper.map(xuatXu, XuatXuAdminResponse.class);
    }

    @Transactional
    public XuatXuAdminResponse updateXuatXu(Integer id, XuatXuAdminRequest xuatXuAdminRequest) {
        XuatXu update = xuatXuRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xuất xứ ID: " + id));

        if (!xuatXuAdminRequest.getMaXuatXu().matches("^[A-Z]+$")) {
            throw new BusinessException("Mã xuất xứ không hợp lệ! Chỉ được phép chứa chữ in hoa (A-Z), không có số, chữ thường hoặc ký tự đặc biệt. Ví dụ hợp lệ:US, VNA");
        }

        if (!xuatXuAdminRequest.getTenQuocGia().matches("^[\\p{L}\\s]+$")) {
            throw new BusinessException(
                    "Tên quốc gia không hợp lệ! Chỉ được chứa chữ cái và khoảng trắng. Ví dụ hợp lệ: 'Việt Nam', 'Hoa Kỳ', 'Nhật Bản'"
            );
        }
        xuatXuAdminRequest.setMaXuatXu(xuatXuAdminRequest.getMaXuatXu().trim());
        xuatXuAdminRequest.setTenQuocGia(xuatXuAdminRequest.getTenQuocGia().trim());

        if (!update.getMaXuatXu().equals(xuatXuAdminRequest.getMaXuatXu()) &&
                xuatXuRepo.existsByMaXuatXuAndIdNot(xuatXuAdminRequest.getMaXuatXu(), id)) {
            throw new BusinessException("Mã xuất xứ đã tồn tại");
        }

        modelMapper.map(xuatXuAdminRequest, update);
        XuatXu saved = xuatXuRepo.save(update);
        return convert(saved);
    }

    @Transactional
    public XuatXuAdminResponse deleteXuatXu(Integer id) {
        XuatXu delete = xuatXuRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xuất xứ ID: " + id));
        xuatXuRepo.deleteById(id);
        return convert(delete);
    }

    public XuatXuAdminResponse detailXuatXu(Integer id) {
        XuatXu detail = xuatXuRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xuất xứ ID: " + id));
        return convert(detail);
    }
}