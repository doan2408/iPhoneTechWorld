package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.RamAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.RamQuickCreateAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.RamAdminResponse;
import org.example.websitetechworld.Entity.Ram;
import org.example.websitetechworld.Repository.RamRepository;
import org.example.websitetechworld.exception.BusinessException;
import org.example.websitetechworld.exception.NotFoundException;
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
public class RamAdminService {
    private final RamRepository ramRepository;
    private final ModelMapper modelMapper;

    private RamAdminResponse convert(Ram ram) {
        return modelMapper.map(ram, RamAdminResponse.class);
    }

    private List<RamAdminResponse> convertList(List<Ram> rams) {
        return rams.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public List<RamAdminResponse> getAllRamList() {
        List<Ram> rams = ramRepository.findAll();
        return convertList(rams);
    }

    public Page<RamAdminResponse> getAllRam(Pageable pageable) {
        Page<Ram> rams = ramRepository.findAll(pageable);
        return rams.map(this::convert);
    }

    public Page<RamAdminResponse> searchRams(String search, Pageable pageable) {
        Page<Ram> ramPage;
        if (StringUtils.hasText(search)) {
            ramPage = ramRepository.findByDungLuongContainingIgnoreCaseOrNhaSanXuatContainingIgnoreCase(search, search, pageable);
        }  else {
            ramPage = ramRepository.findAll(pageable);
        }
        return ramPage.map(this::convert);
    }

    @Transactional
    public RamAdminResponse createRam(RamAdminRequest ramAdminRequest) {
        // Validate dungLuong
        validateRamRequest(ramAdminRequest);
        // Check trùng trong DB
        if (ramRepository.existsByDungLuongAndLoai(ramAdminRequest.getDungLuong(), ramAdminRequest.getLoai())) {
            throw new BusinessException(
                    String.format("Dung lượng RAM %s (%s) đã tồn tại, vui lòng nhập giá trị khác.", ramAdminRequest.getDungLuong(), ramAdminRequest.getLoai())
            );
        }

        Ram ram = modelMapper.map(ramAdminRequest, Ram.class);
        Ram saved = ramRepository.save(ram);
        return convert(saved);
    }


    @Transactional
    public RamAdminResponse createRamQuick(RamQuickCreateAdminRequest ramAdminRequest) {

        String dungLuong = ramAdminRequest.getDungLuong();
        if (dungLuong == null || dungLuong.trim().isEmpty()) {
            throw new BusinessException("Dung lượng RAM không được để trống.");
        }

        if (!dungLuong.trim().matches("^[a-zA-Z0-9]+$")) {
            throw new BusinessException("Dung lượng RAM chỉ được chứa chữ và số, không có khoảng trắng hoặc ký tự đặc biệt.");
        }

        // Validate loại
        String loai = ramAdminRequest.getLoai();
        if (loai == null || loai.trim().isEmpty()) {
            throw new BusinessException("Loại RAM không được để trống.");
        }
        if (!loai.matches("^[a-zA-Z0-9 ]+$")) {
            throw new BusinessException("Loại RAM không được chứa ký tự đặc biệt.");
        }

        if (ramRepository.existsByDungLuongAndLoai(ramAdminRequest.getDungLuong(),ramAdminRequest.getLoai())) {
            throw new BusinessException(
                    String.format("Dung lượng RAM %s (%s) đã tồn tại, vui lòng nhập giá trị khác.",
                            ramAdminRequest.getDungLuong(), ramAdminRequest.getLoai())
            );
        }
        Ram ram = modelMapper.map(ramAdminRequest, Ram.class);
        Ram saved = ramRepository.save(ram);
        return convert(saved);
    }

    @Transactional
    public RamAdminResponse updateRam(Integer id, RamAdminRequest ramAdminRequest) {
        validateRamRequest(ramAdminRequest);
        Ram ram = ramRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy RAM ID: " + id));
        if (ramRepository.existsByDungLuongAndLoaiAndIdNot(ramAdminRequest.getDungLuong(),ramAdminRequest.getLoai(), id)) {
            throw new BusinessException("Dung lượng đã tồn tại");
        }
        modelMapper.map(ramAdminRequest, ram);
        Ram saved = ramRepository.save(ram);
        return convert(saved);
    }

    @Transactional
    public RamAdminResponse deleteRam(Integer id) {
        Ram ram = ramRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy RAM ID: " + id));
        ramRepository.deleteById(id);
        return convert(ram);
    }

    public RamAdminResponse detailRam(Integer id) {
        Ram ram = ramRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy RAM ID: " + id));
        return convert(ram);
    }

    public void validateRamRequest(RamAdminRequest ramAdminRequest) {
        // Validate dung lượng
        String dungLuong = ramAdminRequest.getDungLuong();
        if (dungLuong == null || dungLuong.trim().isEmpty()) {
            throw new BusinessException("Dung lượng RAM không được để trống.");
        }

        if (!dungLuong.trim().matches("^[a-zA-Z0-9]+$")) {
            throw new BusinessException("Dung lượng RAM chỉ được chứa chữ và số, không có khoảng trắng hoặc ký tự đặc biệt.");
        }

        // Validate loại
        String loai = ramAdminRequest.getLoai();
        if (loai == null || loai.trim().isEmpty()) {
            throw new BusinessException("Loại RAM không được để trống.");
        }
        if (!loai.matches("^[a-zA-Z0-9 ]+$")) {
            throw new BusinessException("Loại RAM không được chứa ký tự đặc biệt.");
        }
    }

//    public boolean existsByDungLuong(String dungLuong) {
//        return ramRepository.existsByDungLuong(dungLuong);
//    }
//
//    public boolean existsByDungLuongAndIdNot(String dungLuong, Integer id) {
//        return ramRepository.existsByDungLuongAndIdNot(dungLuong, id);
//    }
}