package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ImeiAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ImeiAdminResponse;
import org.example.websitetechworld.Entity.Imei;
import org.example.websitetechworld.Repository.ImeiReposiory;
import org.example.websitetechworld.Repository.SanPhamChiTietRepository;
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
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ImeiAdminService {
    private final ImeiReposiory imeiReposiory;
    private final SanPhamChiTietRepository sanPhamChiTietRepo;

    private final ModelMapper modelMapper;

    public ImeiAdminResponse convert(Imei imei) {
        return modelMapper.map(imei, ImeiAdminResponse.class);
    }

    public void checkDuplicateImei(ImeiAdminRequest request) {
        Integer count1 = imeiReposiory.countSoImei(request.getSoImei());
        Integer count2 = imeiReposiory.countSoImei2(request.getSoImei2());

        List<Map<String, String>> errors = new ArrayList<>();

        if (count1 != null && count1 > 0) {
            errors.add(Map.of("field", "soImei", "message", "IMEI đã tồn tại"));
        }
        if (count2 != null && count2 > 0) {
            errors.add(Map.of("field", "soImei2", "message", "IMEI 2 đã tồn tại"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public Page<ImeiAdminResponse> getAllImei(Pageable pageable) {
        Page<Imei> imeiList = imeiReposiory.findAll(pageable);
        return imeiList.map(this::convert);
    }

    @Transactional(readOnly = true)
    public List<String> checkImeiDuplicates(List<String> imeis) {
        if (imeis == null || imeis.isEmpty()) {
            throw new BusinessException("Danh sách IMEI không được để trống");
        }

        // Lọc các IMEI hợp lệ (15 chữ số)
        List<String> validImeis = imeis.stream()
                .filter(imei -> imei != null && imei.matches("^\\d{15}$"))
                .collect(Collectors.toList());

        if (validImeis.isEmpty()) {
            throw new BusinessException("Không có IMEI hợp lệ trong danh sách");
        }

        // Kiểm tra trùng lặp
        return imeiReposiory.findBySoImeiIn(validImeis).stream()
                .map(Imei::getSoImei)
                .collect(Collectors.toList());
    }

    public Page<ImeiAdminResponse> getAllImei(Pageable pageable, String keyword) {
        Page<Imei> pageResult;
        if(keyword != null && !keyword.isEmpty()) {
            pageResult = imeiReposiory.findByKeyword(keyword, pageable);
        }
        else {
            pageResult = imeiReposiory.findAll(pageable);
        }
        return pageResult.map(this::convert);
    }

    @Transactional
    public ImeiAdminResponse createImei(ImeiAdminRequest req) {
        List<Map<String, String>> errors = new ArrayList<>();
        //validate
        checkDuplicateImei(req);

        Imei imei = imeiReposiory.save(modelMapper.map(req, Imei.class));
        return convert(imei);
    }

    @Transactional
    public ImeiAdminResponse updateImei(Integer id, ImeiAdminRequest req) {
        Imei imeiOld = imeiReposiory.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Imei ID: " + id));
        List<Map<String, String>> errors = new ArrayList<>();


        boolean unchangeImei1 =
                Objects.equals(req.getSoImei(), imeiOld.getSoImei());

        boolean unchangeImei2 =
                Objects.equals(req.getSoImei2(), imeiOld.getSoImei2());

        // Nếu cả hai đều không thay đổi → không cần kiểm tra trùng lặp → trả về luôn
        if (unchangeImei1 && unchangeImei2) {
            return convert(imeiOld);
        }

        // Nếu có thay đổi ít nhất một trong hai → kiểm tra trùng lặp tương ứng
        if (!unchangeImei1) {
            Integer count1 = imeiReposiory.countSoImei(req.getSoImei());
            if (count1 != null && count1 > 0) {
                errors.add(Map.of("field", "soImei", "message", "IMEI 1 đã tồn tại"));
            }
        }

        if (!unchangeImei2) {
            Integer count2 = imeiReposiory.countSoImei2(req.getSoImei2());
            if (count2 != null && count2 > 0) {
                errors.add(Map.of("field", "soImei2", "message", "IMEI 2 đã tồn tại"));
            }
        }

        // Nếu có lỗi → ném ngoại lệ
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        imeiOld.setSoImei(req.getSoImei());
        imeiOld.setSoImei2(req.getSoImei2());
        imeiOld.setNhaMang(req.getNhaMang());
        imeiOld.setTrangThaiImei(req.getTrangThaiImei());
        imeiOld.setIdSanPhamChiTiet(sanPhamChiTietRepo.findById(req.getIdSanPhamChiTiet()).orElse(null));

        imeiReposiory.save(modelMapper.map(imeiOld, Imei.class));

        return convert(imeiOld);
    }

    @Transactional
    public ImeiAdminResponse deleteImei(Integer id) {
        Imei imei = imeiReposiory.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Imei ID: " + id));
        imeiReposiory.deleteById(id);

        return convert(imei);
    }

    public ImeiAdminResponse detailImei(Integer id) {
        Imei imei = imeiReposiory.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Imei ID: " + id));
        return convert(imei);
    }
}
