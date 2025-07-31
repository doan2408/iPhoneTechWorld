package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HinhAnhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HinhAnhAdminResponse;
import org.example.websitetechworld.Entity.HinhAnh;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Repository.HinhAnhRepository;
import org.example.websitetechworld.Repository.SanPhamChiTietRepository;
import org.example.websitetechworld.Services.CloudinaryService.CloudinaryService;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HinhAnhAdminService {

    private final HinhAnhRepository hinhAnhRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    public HinhAnhAdminResponse convertHinhAnh(HinhAnh hinhAnh) {
        return modelMapper.map(hinhAnh, HinhAnhAdminResponse.class);
    }

    public Page<HinhAnhAdminResponse> getAllHinhAnh(Pageable pageable) {
        Page<HinhAnh> hinhAnhs = hinhAnhRepository.findAll(pageable);
        return hinhAnhs.map(this::convertHinhAnh);
    }

    @Transactional
    public HinhAnhAdminResponse uploadTamThoi(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File ảnh không được để trống");
        }

        try {
            Map<String, String> uploadResult = cloudinaryService.upload(file);

            HinhAnh hinhAnh = new HinhAnh();
            hinhAnh.setUrl(uploadResult.get("url"));
            hinhAnh.setImagePublicId(uploadResult.get("public_id"));

            HinhAnh saved = hinhAnhRepository.save(hinhAnh);

            return convertHinhAnh(saved); // Trả ra url và id ảnh để frontend gán tạm vào màu sắc
        } catch (IOException e) {
            throw new RuntimeException("Lỗi upload ảnh: " + e.getMessage(), e);
        }
    }


    @Transactional
    public HinhAnhAdminResponse updateHinhAnh(Integer id, HinhAnhAdminRequest request, MultipartFile file) {
        try {
            HinhAnh hinhAnh = hinhAnhRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Không thấy hình ảnh ID: " + id));

            // Xoá ảnh cũ khỏi Cloudinary
            if (hinhAnh.getImagePublicId() != null) {
                cloudinaryService.delete(hinhAnh.getImagePublicId());
            }

            Map<String, String> uploadResult = cloudinaryService.upload(file);

            hinhAnh.setUrl(uploadResult.get("url"));
            hinhAnh.setImagePublicId(uploadResult.get("public_id"));

            // Cập nhật liên kết sản phẩm chi tiết nếu cần
            SanPhamChiTiet spct = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm chi tiết"));
            hinhAnh.setIdSanPhamChiTiet(spct);

            return convertHinhAnh(hinhAnhRepository.save(hinhAnh));
        } catch (IOException e) {
            throw new RuntimeException("Lỗi upload ảnh: " + e.getMessage());
        }
    }

    @Transactional
    public HinhAnhAdminResponse deleteHinhAnh(Integer id) {
        HinhAnh hinhAnh = hinhAnhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không thấy hình ảnh ID: " + id));

        try {
            if (hinhAnh.getImagePublicId() != null) {
                cloudinaryService.delete(hinhAnh.getImagePublicId());
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi xoá ảnh trên Cloudinary: " + e.getMessage());
        }

        hinhAnhRepository.deleteById(id);
        return convertHinhAnh(hinhAnh);
    }

    public HinhAnhAdminResponse detailHinhAnh(Integer id) {
        HinhAnh hinhAnh = hinhAnhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không thấy hình ảnh ID: " + id));
        return convertHinhAnh(hinhAnh);
    }

    public Integer countAnh(Integer idSp, Integer idMau) {
        return hinhAnhRepository.anhQuantityOfSp(idSp, idMau);
    }
}
