package org.example.websitetechworld.Services.AdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminNhanVienResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminProductResponse;
import org.example.websitetechworld.Entity.NhanVien;
import org.example.websitetechworld.Repository.NhanVienRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NhanVienAdminService {
    private final NhanVienRepository nhanVienRepository;

    public AdminNhanVienResponse convert(NhanVien nhanVien) {
        AdminNhanVienResponse adminNhanVienResponse = new AdminNhanVienResponse();
        adminNhanVienResponse.setId(nhanVien.getId());
        adminNhanVienResponse.setMaNhanVien(nhanVien.getMaNhanVien());
        adminNhanVienResponse.setTenNhanVien(nhanVien.getTenNhanVien());
        adminNhanVienResponse.setTaiKhoan(nhanVien.getTaiKhoan());
        adminNhanVienResponse.setMatKhau(nhanVien.getMatKhau());
        adminNhanVienResponse.setEmail(nhanVien.getEmail());
        adminNhanVienResponse.setSdt(nhanVien.getSdt());
        adminNhanVienResponse.setDiaChi(nhanVien.getDiaChi());
        adminNhanVienResponse.setTrangThai(nhanVien.getTrangThai().name());
        adminNhanVienResponse.setChucVu(nhanVien.getChucVu().name());
        adminNhanVienResponse.setGioiTinh(nhanVien.getGioiTinh());
        adminNhanVienResponse.setNamSinh(nhanVien.getNamSinh());
        return adminNhanVienResponse;
    }


    public Page<AdminNhanVienResponse> getNhanVienList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);  // Tạo Pageable từ page và size
        Page<NhanVien> pageResult = nhanVienRepository.findAll(pageable);  // Lấy dữ liệu phân trang từ repository
        // Chuyển đổi dữ liệu từ NhanVien thành AdminNhanVienResponse
        return pageResult.map(this::convert);  // Chuyển từng NhanVien thành AdminNhanVienResponse
    }

    public Optional<AdminNhanVienResponse> getStaffById(Integer id) {
        return nhanVienRepository.findById(id).map(this :: convert);
    }
}

