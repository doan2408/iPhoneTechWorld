package org.example.websitetechworld.Services.AdminServices.BaoHanhAdminServices;

import org.example.websitetechworld.Dto.Request.AdminRequest.BaoHanhAdminRequest.LoaiBaoHanhRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.LoaiBaoHanhAdminResponse;
import org.example.websitetechworld.Entity.LoaiBaoHanh;
import org.example.websitetechworld.Entity.ModelSanPham;
import org.example.websitetechworld.Repository.LoaiBaoHanhRepository;
import org.example.websitetechworld.Repository.LoaiRepository;
import org.example.websitetechworld.Repository.ModelSanPhamRepository;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class LoaiBaoHanhAdminServices {
    private final LoaiBaoHanhRepository loaiBaohanhRepository;

    public LoaiBaoHanhAdminServices(LoaiBaoHanhRepository loaiBaohanhRepository, ModelSanPhamRepository modelSanPhamRepository) {
        this.loaiBaohanhRepository = loaiBaohanhRepository;
    }

    // using for dropdown
    public List<LoaiBaoHanhAdminResponse> loaiBaoHanhList() {
        return loaiBaohanhRepository.findAll().stream()
                .map(LoaiBaoHanhAdminResponse::convertDto)
                .toList();
    }

    public Page<LoaiBaoHanhAdminResponse> getAllLoaiBaoHanh(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return loaiBaohanhRepository.findAll(search, pageable)
                .map(loaiBaoHanh -> {
                    LoaiBaoHanhAdminResponse response = new LoaiBaoHanhAdminResponse();
                    response.setId(loaiBaoHanh.getId());
                    response.setIdModelSanPham(loaiBaoHanh.getIdModelSanPham().getIdModelSanPham());
                    response.setTenModelSanPham(loaiBaoHanh.getIdModelSanPham().getTenModel());

                    // Kiểm tra null
                    if (loaiBaoHanh.getIdModelSanPham().getIdXuatXu() != null) {
                        response.setMaXuatXu(loaiBaoHanh.getIdModelSanPham().getIdXuatXu().getMaXuatXu());
                    } else {
                        response.setMaXuatXu("Chưa có xuất xứ");
                    }

                    response.setTenLoaiBaoHanh(loaiBaoHanh.getTenLoaiBaoHanh());
                    response.setThoiGianThang(loaiBaoHanh.getThoiGianThang());
                    response.setMoTa(loaiBaoHanh.getMoTa());
                    return response;
                });
    }

    public LoaiBaoHanhAdminResponse getLoaiBaoHanh(Integer id) {
        LoaiBaoHanh loaiBaoHanh = loaiBaohanhRepository.findById(id).get();
        return LoaiBaoHanhAdminResponse.convertDto(loaiBaoHanh);
    }

    public LoaiBaoHanhAdminResponse addWarrantyType(LoaiBaoHanhRequest request) {
        List<Map<String, String>> errors = new ArrayList<>();
        if (loaiBaohanhRepository.existsByTenLoaiBaoHanhAndIdModel(request.getTenLoaiBaoHanh(),
                request.getIdModelSanPham())
        )
        {
            errors.add(Map.of("field", "loaiBaoHanh", "message", "Loại bảo hành này và model đã được áp dụng"));
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        LoaiBaoHanh loaiBaoHanh = LoaiBaoHanhRequest.convertDto(request);
        loaiBaoHanh.setTenLoaiBaoHanh(request.getTenLoaiBaoHanh().trim());
        LoaiBaoHanh saved = loaiBaohanhRepository.save(loaiBaoHanh);
        return LoaiBaoHanhAdminResponse.convertDto(saved);
    }

    public LoaiBaoHanhAdminResponse updateLoaiBaoHanh(Integer id, LoaiBaoHanhRequest request) {
        LoaiBaoHanh loaiBaoHanhExisting = loaiBaohanhRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loai BaoHanh Not Found"));

        List<Map<String, String>> errors = new ArrayList<>();

        if (
                (!Objects.equals(request.getTenLoaiBaoHanh().trim(), loaiBaoHanhExisting.getTenLoaiBaoHanh())
                || !Objects.equals(request.getIdModelSanPham(), loaiBaoHanhExisting.getIdModelSanPham().getIdModelSanPham())
                )
                && loaiBaohanhRepository.existsByTenLoaiBaoHanhAndIdModel(
                        request.getTenLoaiBaoHanh(),
                        request.getIdModelSanPham()
                )
        ) {
            errors.add(Map.of("field", "loaiBaoHanh", "message", "Loại bảo hành này và model đã được áp dụng"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        ModelSanPham modelSanPham = new ModelSanPham();
        modelSanPham.setIdModelSanPham(request.getIdModelSanPham());

        loaiBaoHanhExisting.setIdModelSanPham(modelSanPham);
        loaiBaoHanhExisting.setTenLoaiBaoHanh(request.getTenLoaiBaoHanh().trim());
        loaiBaoHanhExisting.setThoiGianThang(request.getThoiGianThang());
        loaiBaoHanhExisting.setMoTa(request.getMoTa());

        LoaiBaoHanh updated = loaiBaohanhRepository.save(loaiBaoHanhExisting);
        return LoaiBaoHanhAdminResponse.convertDto(updated);
    }
}
