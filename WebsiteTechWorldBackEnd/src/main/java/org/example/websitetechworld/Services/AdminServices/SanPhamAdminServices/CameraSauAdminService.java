package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CameraSauAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CameraSauQuickCreateAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CameraTruocAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CameraSauAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CameraTruocAdminResponse;
import org.example.websitetechworld.Entity.CameraSau;
import org.example.websitetechworld.Entity.CameraTruoc;
import org.example.websitetechworld.Repository.CameraSauRepository;
import org.example.websitetechworld.exception.BusinessException;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CameraSauAdminService {
    private final CameraSauRepository cameraSauRepository;
    private final ModelMapper modelMapper;

    private CameraSauAdminResponse convert(CameraSau cameraSau) {
        return modelMapper.map(cameraSau, CameraSauAdminResponse.class);
    }

    private List<CameraSauAdminResponse> convertList(List<CameraSau> cameraSau) {
        return cameraSau.stream()
                .map(this::convert)
                .toList();
    }

    public List<CameraSauAdminResponse> getAllCameraSauList() {
        List<CameraSau> cameraSaus = cameraSauRepository.findAll();
        return convertList(cameraSaus);
    }

    public Page<CameraSauAdminResponse> getAllCameraSau(Pageable pageable) {
        Page<CameraSau> cameraSaus = cameraSauRepository.findAll(pageable);
        return cameraSaus.map(this::convert);
    }

    @Transactional
    public CameraSauAdminResponse createCameraSau(CameraSauAdminRequest cameraSauAdminRequest) {
        if (!cameraSauAdminRequest.getLoaiCamera().matches("^[a-zA-Z0-9\\s]+$")) {
            throw new BusinessException("Loại camera không hợp lệ! Chỉ cho phép chữ cái, số và khoảng trắng (không dấu, không ký tự đặc biệt).");
        }

        if (!cameraSauAdminRequest.getDoPhanGiai().matches("^[0-9]+\\s?MP$")) {
            throw new BusinessException("Độ phân giải không hợp lệ! Ví dụ: 12MP, 48MP, 108MP.");
        }

        if (!cameraSauAdminRequest.getKhauDo().matches("^f\\/[0-9]+(\\.[0-9]+)?$")) {
            throw new BusinessException("Khẩu độ không hợp lệ! Ví dụ: f/1.8, f/2.2.");
        }

        if (cameraSauRepository.existsByLoaiCameraAndDoPhanGiaiAndKhauDo(
                cameraSauAdminRequest.getLoaiCamera(),
                cameraSauAdminRequest.getDoPhanGiai(),
                cameraSauAdminRequest.getKhauDo()
        )) {
            throw new BusinessException(String.format(
                    "Camera sau với loại: '%s', độ phân giải: '%s', khẩu độ: '%s' đã tồn tại.",
                    cameraSauAdminRequest.getLoaiCamera(),
                    cameraSauAdminRequest.getDoPhanGiai(),
                    cameraSauAdminRequest.getKhauDo()
            ));
        }

        CameraSau cameraSau = cameraSauRepository.save(modelMapper.map(cameraSauAdminRequest, CameraSau.class));
        return convert(cameraSau);
    }

    @Transactional
    public CameraSauAdminResponse createCameraSauQuick(CameraSauQuickCreateAdminRequest cameraSauAdminRequest) {

        if (!cameraSauAdminRequest.getLoaiCamera().matches("^[a-zA-Z0-9\\s]+$")) {
            throw new BusinessException("Loại camera không hợp lệ! Chỉ cho phép chữ cái, số và khoảng trắng (không dấu, không ký tự đặc biệt).");
        }

        if (!cameraSauAdminRequest.getDoPhanGiai().matches("^[0-9]+\\s?MP$")) {
            throw new BusinessException("Độ phân giải không hợp lệ! Ví dụ: 12MP, 48MP, 108MP.");
        }

        if (!cameraSauAdminRequest.getKhauDo().matches("^f\\/[0-9]+(\\.[0-9]+)?$")) {
            throw new BusinessException("Khẩu độ không hợp lệ! Ví dụ: f/1.8, f/2.2.");
        }


        if (cameraSauRepository.existsByLoaiCameraAndDoPhanGiaiAndKhauDo(
                cameraSauAdminRequest.getLoaiCamera(),
                cameraSauAdminRequest.getDoPhanGiai(),
                cameraSauAdminRequest.getKhauDo()
        )) {
            throw new BusinessException(String.format(
                    "Camera sau với loại: '%s', độ phân giải: '%s', khẩu độ: '%s' đã tồn tại.",
                    cameraSauAdminRequest.getLoaiCamera(),
                    cameraSauAdminRequest.getDoPhanGiai(),
                    cameraSauAdminRequest.getKhauDo()
            ));
        }

        CameraSau cameraSau = cameraSauRepository.save(modelMapper.map(cameraSauAdminRequest, CameraSau.class));
        return convert(cameraSau);
    }

    @Transactional
    public CameraSauAdminResponse updateCameraSau(Integer id, CameraSauAdminRequest cameraSauAdminRequest) {

        if (!cameraSauAdminRequest.getLoaiCamera().matches("^[a-zA-Z0-9\\s]+$")) {
            throw new BusinessException("Loại camera không hợp lệ! Chỉ cho phép chữ cái, số và khoảng trắng (không dấu, không ký tự đặc biệt).");
        }

        if (!cameraSauAdminRequest.getDoPhanGiai().matches("^[0-9]+\\s?MP$")) {
            throw new BusinessException("Độ phân giải không hợp lệ! Ví dụ: 12MP, 48MP, 108MP.");
        }

        if (!cameraSauAdminRequest.getKhauDo().matches("^f\\/[0-9]+(\\.[0-9]+)?$")) {
            throw new BusinessException("Khẩu độ không hợp lệ! Ví dụ: f/1.8, f/2.2.");
        }


        if (cameraSauRepository.existsByLoaiCameraAndDoPhanGiaiAndKhauDoAndIdNot(cameraSauAdminRequest.getLoaiCamera(), cameraSauAdminRequest.getDoPhanGiai(), cameraSauAdminRequest.getKhauDo(), cameraSauAdminRequest.getId())) {
            throw new BusinessException(String.format(
                    "Camera sau với loại: '%s', độ phân giải: '%s', khẩu độ: '%s' đã tồn tại.",
                    cameraSauAdminRequest.getLoaiCamera(),
                    cameraSauAdminRequest.getDoPhanGiai(),
                    cameraSauAdminRequest.getKhauDo()
            ));
        }

        CameraSau cameraSau = cameraSauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Camera sau ID: " + id));
        modelMapper.map(cameraSauAdminRequest, cameraSau);
        cameraSauRepository.save(cameraSau);

        return convert(cameraSau);
    }

    @Transactional
    public CameraSauAdminResponse deleteCameraSau(Integer id) {
        CameraSau cameraSau = cameraSauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Camera sau ID: " + id));

        cameraSauRepository.deleteById(id);

        return convert(cameraSau);
    }

    public CameraSauAdminResponse detailCameraSau(Integer id) {
        CameraSau cameraSau = cameraSauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Camera sau ID: " + id));

        return convert(cameraSau);
    }
}
