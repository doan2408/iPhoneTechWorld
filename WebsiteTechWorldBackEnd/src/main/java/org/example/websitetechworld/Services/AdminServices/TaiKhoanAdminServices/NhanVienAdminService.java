package org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest.AdminStaffRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminNhanVienResponse;
import org.example.websitetechworld.Entity.NhanVien;
import org.example.websitetechworld.Repository.NhanVienRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NhanVienAdminService {
    private final NhanVienRepository nhanVienRepository;

    public AdminNhanVienResponse convertResponse(NhanVien nhanVien) {
        AdminNhanVienResponse adminNhanVienResponse = new AdminNhanVienResponse();
        adminNhanVienResponse.setId(nhanVien.getId());
        adminNhanVienResponse.setMaNhanVien(nhanVien.getMaNhanVien());
        adminNhanVienResponse.setTenNhanVien(nhanVien.getTenNhanVien());
        adminNhanVienResponse.setTaiKhoan(nhanVien.getTaiKhoan());
        adminNhanVienResponse.setMatKhau(nhanVien.getMatKhau());
        adminNhanVienResponse.setEmail(nhanVien.getEmail());
        adminNhanVienResponse.setSdt(nhanVien.getSdt());
        adminNhanVienResponse.setDiaChi(nhanVien.getDiaChi());
        adminNhanVienResponse.setTrangThai(nhanVien.getTrangThai());
        adminNhanVienResponse.setChucVu(nhanVien.getChucVu());
        adminNhanVienResponse.setGioiTinh(nhanVien.getGioiTinh());
        adminNhanVienResponse.setNamSinh(nhanVien.getNamSinh());
        return adminNhanVienResponse;
    }

    //hien thi nhan vien
    public Page<AdminNhanVienResponse> getNhanVienList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);  // Tạo Pageable từ page và size
        Page<NhanVien> pageResult = nhanVienRepository.findAll(pageable);  // Lấy dữ liệu phân trang từ repository
        // Chuyển đổi dữ liệu từ NhanVien thành AdminNhanVienResponse
        return pageResult.map(this::convertResponse);  // Chuyển từng NhanVien thành AdminNhanVienResponse
    }

    //detail nhan vien
    public Optional<AdminNhanVienResponse> getStaffById(Integer id) {
        return nhanVienRepository.findById(id).map(this :: convertResponse);
    }

    //convert staffRequest -> staffEntity
    public void updateNhanVienFromRequest(NhanVien nhanVien, AdminStaffRequest adminStaffRequest) {
        nhanVien.setMaNhanVien(adminStaffRequest.getMaNhanVien());
        nhanVien.setTenNhanVien(adminStaffRequest.getTenNhanVien());
        nhanVien.setTaiKhoan(adminStaffRequest.getTaiKhoan());
        nhanVien.setEmail(adminStaffRequest.getEmail());
        nhanVien.setSdt(adminStaffRequest.getSdt());
        nhanVien.setDiaChi(adminStaffRequest.getDiaChi());
        nhanVien.setTrangThai(adminStaffRequest.getTrangThai());
        nhanVien.setChucVu(adminStaffRequest.getChucVu());
        nhanVien.setGioiTinh(adminStaffRequest.getGioiTinh());
        nhanVien.setNamSinh(adminStaffRequest.getNamSinh());
    }


    public NhanVien updateStaff(Integer id, AdminStaffRequest staffRequest) {
        NhanVien nhanvienExisting = nhanVienRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("khong tim thay nhan vien voi id: " + id));
        updateNhanVienFromRequest(nhanvienExisting, staffRequest);
        return nhanVienRepository.save(nhanvienExisting);
    }
}

