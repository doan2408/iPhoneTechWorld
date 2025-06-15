package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ManHinhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ManHinhAdminResponse;
import org.example.websitetechworld.Entity.ManHinh;
import org.example.websitetechworld.Entity.MauSac;
import org.example.websitetechworld.Repository.ManHinhRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.example.websitetechworld.exception.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ManHinhAdminService {
    private final ManHinhRepository manHinhRepository;
    private final ModelMapper modelMapper;

    private ManHinhAdminResponse convert(ManHinh manHinh) {
        return modelMapper.map(manHinh, ManHinhAdminResponse.class);
    }

    private List<ManHinhAdminResponse> convertList(List<ManHinh> manHinh) {
        return manHinh.stream()
                .map(this::convert)
                .toList();
    }

    public List<ManHinhAdminResponse> getAllManHinhList() {
        List<ManHinh> manHinhs = manHinhRepository.findAll();
        return convertList(manHinhs);
    }

    //hiển thị màn hình phân trang
    public Page<ManHinhAdminResponse> getAllManHinh(Pageable pageable) {
        Page<ManHinh> manHinhs = manHinhRepository.findAll(pageable);
        return manHinhs.map(this::convert);
    }

    //search, hiển thị, phân trang man hinh
    public Page<ManHinhAdminResponse> getAllManHinh(Pageable pageable, String keyword) {
        Page<ManHinh> pageResult;
        if (keyword != null && !keyword.trim().isEmpty()) {
            pageResult = manHinhRepository.findByKeyword(keyword, pageable);
        } else {
            pageResult = manHinhRepository.findAll(pageable);
        }
        return pageResult.map(this::convert);
    }

    @Transactional
    public ManHinhAdminResponse createManHinh(ManHinhAdminRequest manHinhAdminRequest) {
        List<Map<String, String>> errors = new ArrayList<>();

        Integer count = manHinhRepository.countCheckTrung(
                manHinhAdminRequest.getTenManHinh(),
                manHinhAdminRequest.getKichThuoc(),
                manHinhAdminRequest.getLoaiManHinh(),
                manHinhAdminRequest.getDoPhanGiai(),
                manHinhAdminRequest.getTanSoQuet(),
                manHinhAdminRequest.getDoSang(),
                manHinhAdminRequest.getChatLieuKinh()
        );
        if (count > 0) {
            errors.add(Map.of("field", "errorsThuocTinh", "message", "Màn hình có những thuộc tính này đã tồn tại!"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        ManHinh manHinh = manHinhRepository.save(modelMapper.map(manHinhAdminRequest, ManHinh.class));
        return convert(manHinh);
    }

    @Transactional
    public ManHinhAdminResponse updateManHinh(Integer id, ManHinhAdminRequest req) {
        ManHinh manHinhOld = manHinhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màn hình ID: " + id));

        List<Map<String, String>> errors = new ArrayList<>();

        // Kiểm tra xem có sự thay đổi không
        boolean unchanged =
                Objects.equals(req.getTenManHinh(), manHinhOld.getTenManHinh()) &&
                        Objects.equals(req.getKichThuoc(), manHinhOld.getKichThuoc()) &&
                        Objects.equals(req.getLoaiManHinh(), manHinhOld.getLoaiManHinh()) &&
                        Objects.equals(req.getDoPhanGiai(), manHinhOld.getDoPhanGiai()) &&
                        Objects.equals(req.getTanSoQuet(), manHinhOld.getTanSoQuet()) &&
                        Objects.equals(req.getDoSang(), manHinhOld.getDoSang()) &&
                        Objects.equals(req.getChatLieuKinh(), manHinhOld.getChatLieuKinh());
        //không có sự thay đổi -> trả về dữ liệu cũ
        if (unchanged) {
            return convert(manHinhOld);
        }
        else {
            Integer count = manHinhRepository.countCheckTrung(
                    req.getTenManHinh(),
                    req.getKichThuoc(),
                    req.getLoaiManHinh(),
                    req.getDoPhanGiai(),
                    req.getTanSoQuet(),
                    req.getDoSang(),
                    req.getChatLieuKinh()
            );
            if (count > 0) {
                errors.add(Map.of("field", "errorsThuocTinh", "message", "Màn hình có những thuộc tính này đã tồn tại!"));
            }

            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }

            modelMapper.map(req, manHinhOld);
            manHinhRepository.save(manHinhOld);
            return convert(manHinhOld);
        }
    }

    @Transactional
    public ManHinhAdminResponse deleteManHinh(Integer id) {
        ManHinh manHinh = manHinhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màn hình ID: " + id));
        manHinhRepository.deleteById(id);

        return convert(manHinh);
    }

    public ManHinhAdminResponse detailManHinh(Integer id) {
        ManHinh manHinh = manHinhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màn hình ID: " + id));

        return convert(manHinh);
    }
}
