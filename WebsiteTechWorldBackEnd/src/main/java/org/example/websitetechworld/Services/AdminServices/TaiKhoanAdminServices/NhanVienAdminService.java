package org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest.AdminStaffRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminNhanVienResponse;
import org.example.websitetechworld.Entity.NhanVien;
import org.example.websitetechworld.Repository.NhanVienRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NhanVienAdminService {
    private final NhanVienRepository nhanVienRepository;
    private final PasswordEncoder passwordEncoder;

    //convert NhanVienEntity ----> AdminNhanVienResponse
    public AdminNhanVienResponse convertToResponse(NhanVien nhanVien) {
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

    //convert NhanVienEntity ----> AdminStaffRequest
    public AdminStaffRequest convertToRequest(NhanVien nv) {
        AdminStaffRequest dto = new AdminStaffRequest();
        dto.setId(nv.getId());
        dto.setMaNhanVien(nv.getMaNhanVien());
        dto.setTenNhanVien(nv.getTenNhanVien());
        dto.setTaiKhoan(nv.getTaiKhoan());
        dto.setEmail(nv.getEmail());
        dto.setSdt(nv.getSdt());
        dto.setDiaChi(nv.getDiaChi());
        dto.setTrangThai(nv.getTrangThai());
        dto.setChucVu(nv.getChucVu());
        dto.setGioiTinh(nv.getGioiTinh());
        dto.setNamSinh(nv.getNamSinh());
        return dto;
    }

    //convert staffRequest -> staffEntity (Dành cho update: không có trường mật khẩu)
    public void convertNhanVienFromRequest(NhanVien nhanVien, AdminStaffRequest adminStaffRequest, Boolean isAdd) {
        nhanVien.setMaNhanVien(adminStaffRequest.getMaNhanVien());
        nhanVien.setTenNhanVien(adminStaffRequest.getTenNhanVien());
        nhanVien.setTaiKhoan(adminStaffRequest.getTaiKhoan());
        if (isAdd) {
            // Khi là add, mật khẩu phải được set
            if (adminStaffRequest.getMatKhau() != null && !adminStaffRequest.getMatKhau().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(adminStaffRequest.getMatKhau());
                nhanVien.setMatKhau(encodedPassword);
            }
        } else {
            // Khi là update, chỉ cập nhật mật khẩu nếu nó được truyền từ request
            if (adminStaffRequest.getMatKhau() != null && !adminStaffRequest.getMatKhau().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(adminStaffRequest.getMatKhau());
                nhanVien.setMatKhau(encodedPassword);
            }
        }
        nhanVien.setEmail(adminStaffRequest.getEmail());
        nhanVien.setSdt(adminStaffRequest.getSdt());
        nhanVien.setDiaChi(adminStaffRequest.getDiaChi());
        nhanVien.setTrangThai(adminStaffRequest.getTrangThai());
        nhanVien.setChucVu(adminStaffRequest.getChucVu());
        nhanVien.setGioiTinh(adminStaffRequest.getGioiTinh());
        nhanVien.setNamSinh(adminStaffRequest.getNamSinh());
    }

    //hien thi nhan vien (màn admin: không có mật khẩu)
    public Page<AdminNhanVienResponse> getNhanVienList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);  // Tạo Pageable từ page và size
        Page<NhanVien> pageResult = nhanVienRepository.findAll(pageable);  // Lấy dữ liệu phân trang từ repository
        // Chuyển đổi dữ liệu từ NhanVien thành AdminNhanVienResponse
        return pageResult.map(this::convertToResponse);  // Chuyển từng NhanVien thành AdminNhanVienResponse
    }

    //detail nhan vien
    public Optional<AdminNhanVienResponse> getStaffById(Integer id) {
        return nhanVienRepository.findById(id).map(this :: convertToResponse);
    }


    //update nhân viên (màn admin)
    public AdminStaffRequest updateStaff(Integer id, AdminStaffRequest staffRequest) {
        NhanVien nhanvienExisting = nhanVienRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("khong tim thay nhan vien voi id: " + id));
        convertNhanVienFromRequest(nhanvienExisting, staffRequest, false);
        NhanVien nhanVienUpdate =  nhanVienRepository.save(nhanvienExisting);
        return convertToRequest(nhanVienUpdate);
    }


    //add nhan vien
    public AdminStaffRequest createStaff(AdminStaffRequest staffRequest) {
        NhanVien nhanVien = new NhanVien();
        convertNhanVienFromRequest(nhanVien, staffRequest, true);
        NhanVien nvAdd = nhanVienRepository.save(nhanVien);
        return convertToRequest(nvAdd);

    }

    //xoa nhan vien
    public void deleteStaff(Integer id) {
         if(!nhanVienRepository.existsById(id)) {
            throw new RuntimeException("Not found staff id: " + id);
         }
         nhanVienRepository.deleteById(id);
    }

}

