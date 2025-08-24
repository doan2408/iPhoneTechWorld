package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CameraTruocAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CameraTruocQuickCreateAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CameraTruocAdminResponse;
import org.example.websitetechworld.Entity.CameraTruoc;
import org.example.websitetechworld.Repository.CameraTruocRepository;
import org.example.websitetechworld.exception.BusinessException;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.example.websitetechworld.exception.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CameraTruocAdminService {
    private final CameraTruocRepository cameraTruocRepository;
    private final ModelMapper modelMapper;

    private CameraTruocAdminResponse convert(CameraTruoc cameraTruoc) {
        return modelMapper.map(cameraTruoc, CameraTruocAdminResponse.class);
    }

    private List<CameraTruocAdminResponse> convertList(List<CameraTruoc> cameraTruoc) {
        return cameraTruoc.stream()
                .map(this::convert)
                .toList();
    }

    public List<CameraTruocAdminResponse> getAllCameraTruocList() {
        List<CameraTruoc> cameraTruocs = cameraTruocRepository.findAll();
        return convertList(cameraTruocs);
    }

    //hiển thị màn hình camera trước
    public Page<CameraTruocAdminResponse> getAllCameraTruoc(Pageable pageable) {
        Page<CameraTruoc> cameraTruocs = cameraTruocRepository.findAll(pageable);
        return cameraTruocs.map(this::convert);
    }

    public Page<CameraTruocAdminResponse> getAllCameraTruoc(Pageable pageable, String keyword) {
        Page<CameraTruoc> pageResult;

        if (keyword != null && !keyword.isEmpty()) {
            pageResult = cameraTruocRepository.findByKeyword(keyword, pageable);
        }
        else {
            pageResult = cameraTruocRepository.findAll(pageable);
        }
        return pageResult.map(this::convert);
    }

    @Transactional
    public CameraTruocAdminResponse createCameraTruoc(CameraTruocAdminRequest cameraTruocAdminRequest) {

        if (!cameraTruocAdminRequest.getLoaiCamera().matches("^[a-zA-Z0-9\\s]+$")) {
            throw new BusinessException("Loại camera không hợp lệ! Chỉ cho phép chữ cái, số và khoảng trắng (không dấu, không ký tự đặc biệt).");
        }

        if (!cameraTruocAdminRequest.getDoPhanGiai().matches("^[0-9]+\\s?MP$")) {
            throw new BusinessException("Độ phân giải không hợp lệ! Ví dụ: 12MP, 48MP, 108MP.");
        }

        if (!cameraTruocAdminRequest.getKhauDo().matches("^f\\/[0-9]+(\\.[0-9]+)?$")) {
            throw new BusinessException("Khẩu độ không hợp lệ! Ví dụ: f/1.8, f/2.2.");
        }


        if (cameraTruocRepository.existsByLoaiCameraAndDoPhanGiaiAndKhauDo(
                cameraTruocAdminRequest.getLoaiCamera(),
                cameraTruocAdminRequest.getDoPhanGiai(),
                cameraTruocAdminRequest.getKhauDo())) {

            throw new BusinessException(
                    String.format("Camera trước với loại [%s], độ phân giải [%s], khẩu độ [%s] đã tồn tại!",
                            cameraTruocAdminRequest.getLoaiCamera(),
                            cameraTruocAdminRequest.getDoPhanGiai(),
                            cameraTruocAdminRequest.getKhauDo()
                    )
            );
        }

        CameraTruoc cameraTruoc = cameraTruocRepository.save(modelMapper.map(cameraTruocAdminRequest, CameraTruoc.class));
        return convert(cameraTruoc);
    }


    @Transactional
    public CameraTruocAdminResponse createCameraTruocQuick(CameraTruocQuickCreateAdminRequest cameraTruocAdminRequest) {

        if (!cameraTruocAdminRequest.getLoaiCamera().matches("^[a-zA-Z0-9\\s]+$")) {
            throw new BusinessException("Loại camera không hợp lệ! Chỉ cho phép chữ cái, số và khoảng trắng (không dấu, không ký tự đặc biệt).");
        }

        if (!cameraTruocAdminRequest.getDoPhanGiai().matches("^[0-9]+\\s?MP$")) {
            throw new BusinessException("Độ phân giải không hợp lệ! Ví dụ: 12MP, 48MP, 108MP.");
        }

        if (!cameraTruocAdminRequest.getKhauDo().matches("^f\\/[0-9]+(\\.[0-9]+)?$")) {
            throw new BusinessException("Khẩu độ không hợp lệ! Ví dụ: f/1.8, f/2.2.");
        }

        if (cameraTruocRepository.existsByLoaiCameraAndDoPhanGiaiAndKhauDo(
                cameraTruocAdminRequest.getLoaiCamera(),
                cameraTruocAdminRequest.getDoPhanGiai(),
                cameraTruocAdminRequest.getKhauDo())) {

            throw new BusinessException(
                    String.format("Camera trước với loại [%s], độ phân giải [%s], khẩu độ [%s] đã tồn tại!",
                            cameraTruocAdminRequest.getLoaiCamera(),
                            cameraTruocAdminRequest.getDoPhanGiai(),
                            cameraTruocAdminRequest.getKhauDo()
                    )
            );
        }


        CameraTruoc cameraTruoc = cameraTruocRepository.save(modelMapper.map(cameraTruocAdminRequest, CameraTruoc.class));
        return convert(cameraTruoc);
    }

    @Transactional
    public CameraTruocAdminResponse updateCameraTruoc(Integer id, CameraTruocAdminRequest cameraTruocAdminRequest) {
        CameraTruoc cameraTruocOld = cameraTruocRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Camera trước ID: " + id));
        if (cameraTruocRepository.existsByLoaiCameraAndDoPhanGiaiAndKhauDoAndIdNot(
                cameraTruocAdminRequest.getLoaiCamera(),
                cameraTruocAdminRequest.getDoPhanGiai(),
                cameraTruocAdminRequest.getKhauDo(),
                cameraTruocAdminRequest.getId())) {

            throw new BusinessException(
                    String.format("Camera trước với loại [%s], độ phân giải [%s], khẩu độ [%s] đã tồn tại!",
                            cameraTruocAdminRequest.getLoaiCamera(),
                            cameraTruocAdminRequest.getDoPhanGiai(),
                            cameraTruocAdminRequest.getKhauDo()
                    )
            );
        }
        modelMapper.map(cameraTruocAdminRequest, cameraTruocOld);
        cameraTruocRepository.save(cameraTruocOld);

        return convert(cameraTruocOld);
    }

    @Transactional
    public CameraTruocAdminResponse deleteCameraTruoc(Integer id) {
        CameraTruoc cameraTruoc = cameraTruocRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Camera trước ID: " + id));

        cameraTruocRepository.deleteById(id);

        return convert(cameraTruoc);
    }

    public CameraTruocAdminResponse detailCameraTruoc(Integer id) {
        CameraTruoc cameraTruoc = cameraTruocRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Camera trước ID: " + id));

        return convert(cameraTruoc);
    }
}
