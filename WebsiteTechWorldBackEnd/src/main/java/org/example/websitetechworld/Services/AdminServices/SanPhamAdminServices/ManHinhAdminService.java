package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ManHinhAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ManHinhQuickCreateAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ManHinhAdminResponse;
import org.example.websitetechworld.Entity.ManHinh;
import org.example.websitetechworld.Entity.MauSac;
import org.example.websitetechworld.Repository.ManHinhRepository;
import org.example.websitetechworld.exception.BusinessException;
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

        ValidateManHinh(manHinhAdminRequest);


        String tenNormalized = manHinhAdminRequest.getTenManHinh()
                .replaceAll("\\s+", "")
                .toLowerCase();

        if (manHinhRepository.existsByTenManHinhAndKichThuocAndLoaiManHinh(
                tenNormalized,
                manHinhAdminRequest.getKichThuoc(),
                manHinhAdminRequest.getLoaiManHinh()
        )) {
            throw new BusinessException(
                    String.format(
                            "Màn hình '%s' với kích thước %s và loại %s đã tồn tại.",
                            manHinhAdminRequest.getTenManHinh(),
                            manHinhAdminRequest.getKichThuoc(),
                            manHinhAdminRequest.getLoaiManHinh()
                    )
            );
        }
        ManHinh manHinh = manHinhRepository.save(modelMapper.map(manHinhAdminRequest, ManHinh.class));
        return convert(manHinh);
    }

    @Transactional
    public ManHinhAdminResponse createManHinhQuick (ManHinhQuickCreateAdminRequest manHinhAdminRequest) {
        String ten = manHinhAdminRequest.getTenManHinh().trim();
        if (!ten.matches("^[a-zA-Z0-9 ]+$")) {
            throw new BusinessException("Tên màn hình không hợp lệ. Chỉ được chứa chữ cái không dấu (A-Z, a-z), số (0-9) và khoảng trắng, không được chứa ký tự đặc biệt.");
        }

        manHinhAdminRequest.setTenManHinh(ten.trim());

        String kichThuoc = manHinhAdminRequest.getKichThuoc();
        if (!kichThuoc.matches("^[0-9.\\sA-Za-z\\\"]+$")) {
            throw new BusinessException("Kích thước không được chứa ký tự đặc biệt.");
        }
        if (!kichThuoc.matches("^[0-9]+(\\.[0-9]+)? inch$")) {
            throw new BusinessException("Kích thước phải là số (thập phân) và bắt buộc kèm ' inch' với đúng 1 khoảng trắng. Ví dụ: 6.1 inch.");
        }

        manHinhAdminRequest.setKichThuoc(manHinhAdminRequest.getKichThuoc().trim());

        String loai = manHinhAdminRequest.getLoaiManHinh();
        if (!loai.matches("^[A-Z0-9]+$")) {
            throw new BusinessException("Loại màn hình không hợp lệ. Chỉ được chứa chữ cái viết hoa (A-Z) và số (0-9), không khoảng trắng hay ký tự đặc biệt.");
        }

        manHinhAdminRequest.setLoaiManHinh(manHinhAdminRequest.getLoaiManHinh().trim());

        String tenNormalized = manHinhAdminRequest.getTenManHinh()
                .replaceAll("\\s+", "")
                .toLowerCase();

        if (manHinhRepository.existsByTenManHinhAndKichThuocAndLoaiManHinh(
                tenNormalized,
                manHinhAdminRequest.getKichThuoc(),
                manHinhAdminRequest.getLoaiManHinh()
        )) {
            throw new BusinessException(
                    String.format(
                            "Màn hình '%s' với kích thước %s và loại %s đã tồn tại.",
                            manHinhAdminRequest.getTenManHinh(),
                            manHinhAdminRequest.getKichThuoc(),
                            manHinhAdminRequest.getLoaiManHinh()
                    )
            );
        }
        ManHinh manHinh = manHinhRepository.save(modelMapper.map(manHinhAdminRequest, ManHinh.class));
        return convert(manHinh);
    }

    @Transactional
    public ManHinhAdminResponse updateManHinh(Integer id, ManHinhAdminRequest manHinhAdminRequest) {
        ManHinh manHinhOld = manHinhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màn hình ID: " + id));

        ValidateManHinh(manHinhAdminRequest);

        String tenNormalized = manHinhAdminRequest.getTenManHinh()
                .replaceAll("\\s+", "")
                .toLowerCase();

        if (manHinhRepository.existsByTenManHinhNormalizedAndKichThuocAndLoaiManHinhAndIdNot(
                tenNormalized,
                manHinhAdminRequest.getKichThuoc(),
                manHinhAdminRequest.getLoaiManHinh(),
                manHinhAdminRequest.getId()
        )) {
            throw new BusinessException(
                    String.format(
                            "Màn hình '%s' với kích thước %s và loại %s đã tồn tại.",
                            manHinhAdminRequest.getTenManHinh(),
                            manHinhAdminRequest.getKichThuoc(),
                            manHinhAdminRequest.getLoaiManHinh()
                    )
            );
        }
            modelMapper.map(manHinhAdminRequest, manHinhOld);
            manHinhRepository.save(manHinhOld);
            return convert(manHinhOld);
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

    public void ValidateManHinh (ManHinhAdminRequest manHinhAdminRequest) {
        String ten = manHinhAdminRequest.getTenManHinh().trim();
        if (!ten.matches("^[a-zA-Z0-9 ]+$")) {
            throw new BusinessException("Tên màn hình không hợp lệ. Chỉ được chứa chữ cái không dấu (A-Z, a-z), số (0-9) và khoảng trắng, không được chứa ký tự đặc biệt.");
        }

        manHinhAdminRequest.setTenManHinh(ten.trim());

        String kichThuoc = manHinhAdminRequest.getKichThuoc().trim();
        if (!kichThuoc.matches("^[0-9.\\sA-Za-z\\\"]+$")) {
            throw new BusinessException("Kích thước không được chứa ký tự đặc biệt.");
        }
        if (!kichThuoc.matches("^[0-9]+(\\.[0-9]+)? inch$")) {
            throw new BusinessException("Kích thước phải là số (thập phân) và bắt buộc kèm ' inch' với đúng 1 khoảng trắng. Ví dụ: 6.1 inch.");
        }

        manHinhAdminRequest.setKichThuoc(manHinhAdminRequest.getKichThuoc().trim());

        String loai = manHinhAdminRequest.getLoaiManHinh().trim();
        if (!loai.matches("^[A-Z0-9]+$")) {
            throw new BusinessException("Loại màn hình không hợp lệ. Chỉ được chứa chữ cái viết hoa (A-Z) và số (0-9), không khoảng trắng hay ký tự đặc biệt.");
        }

        manHinhAdminRequest.setLoaiManHinh(manHinhAdminRequest.getLoaiManHinh().trim());

        if (!manHinhAdminRequest.getDoPhanGiai().trim().matches("^\\d+x\\d+$")) {
            throw new BusinessException("Độ phân giải không hợp lệ. Ví dụ: 1792x828");
        }
        manHinhAdminRequest.setDoPhanGiai(manHinhAdminRequest.getDoPhanGiai().trim());

        if (!manHinhAdminRequest.getTanSoQuet().trim().matches("^\\d+Hz$")) {
            throw new BusinessException("Tần số quét không hợp lệ. Ví dụ: 60Hz");
        }
        manHinhAdminRequest.setTanSoQuet(manHinhAdminRequest.getTanSoQuet().trim());

        if (!manHinhAdminRequest.getDoSang().trim().matches("^\\d+ nit$")) {
            throw new BusinessException("Độ sáng không hợp lệ. Ví dụ: 700 nit");
        }
        manHinhAdminRequest.setDoSang(manHinhAdminRequest.getDoSang().trim());

        if (!manHinhAdminRequest.getChatLieuKinh().trim().matches("^[a-zA-Z ]+$")) {
            throw new BusinessException("Kính bảo vệ không hợp lệ. Ví dụ: Ceramic Shield");
        }
        manHinhAdminRequest.setChatLieuKinh(manHinhAdminRequest.getChatLieuKinh().trim());
    }
}
