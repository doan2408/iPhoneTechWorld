package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HDHQuickCreateAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HeDieuHanhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HeDieuHanhAdminResponse;
import org.example.websitetechworld.Entity.HeDieuHanh;
import org.example.websitetechworld.Entity.ManHinh;
import org.example.websitetechworld.Repository.HeDieuHanhRepository;
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
        List<Map<String, String>> errors = new ArrayList<>();


            Integer count = heDieuHanhRepository.countCheckTrung(
                    heDieuHanhAdminRequest.getPhienBan());
            if (count > 0) {
                errors.add(Map.of("field", "phienBan", "message", "Phiên bản này đã tồn tại"));
            }

            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
            HeDieuHanh heDieuHanh = heDieuHanhRepository.save(modelMapper.map(heDieuHanhAdminRequest, HeDieuHanh.class));
            return convert(heDieuHanh);
    }

    @Transactional
    public HeDieuHanhAdminResponse createHDHQuick (HDHQuickCreateAdminRequest request) {
        HeDieuHanh dieuHanh = heDieuHanhRepository.save(modelMapper.map(request, HeDieuHanh.class));
        return convert(dieuHanh);
    }


    @Transactional
    public HeDieuHanhAdminResponse updateHeDieuHanh(Integer id, HeDieuHanhAdminRequest heDieuHanhAdminRequest) {
        HeDieuHanh dieuHanhOld = heDieuHanhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hệ điều hành ID: " + id));

        List<Map<String, String>> errors = new ArrayList<>();
        boolean unchanged =
                Objects.equals(heDieuHanhAdminRequest.getPhienBan(), dieuHanhOld.getPhienBan()) &&
                        Objects.equals(heDieuHanhAdminRequest.getGiaoDienNguoiDung(), dieuHanhOld.getGiaoDienNguoiDung()) &&
                        Objects.equals(heDieuHanhAdminRequest.getNhaPhatTrien(), dieuHanhOld.getNhaPhatTrien());
        //không có sự thay đổi -> trả về dữ liệu cũ
        if(unchanged) {
            return convert(dieuHanhOld);
        }
        else {
            Integer count = heDieuHanhRepository.countCheckTrung(
                    heDieuHanhAdminRequest.getPhienBan());
            if (count > 0) {
                errors.add(Map.of("field", "phienBan", "message", "Phiên bản này đã tồn tại"));
            }

            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
            modelMapper.map(heDieuHanhAdminRequest, dieuHanhOld);
            heDieuHanhRepository.save(dieuHanhOld);
            return convert(dieuHanhOld);
        }
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
