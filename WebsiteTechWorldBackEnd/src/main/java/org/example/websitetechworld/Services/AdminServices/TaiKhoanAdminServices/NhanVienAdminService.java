package org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest.AdminStaffRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminNhanVienResponse;
import org.example.websitetechworld.Entity.NhanVien;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.NhanVienRepository;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NhanVienAdminService {
    private final NhanVienRepository nhanVienRepository;
    private final PasswordEncoder passwordEncoder;
    private final KhachHangRepository khachHangRepository;

    //convert NhanVienEntity ----> AdminNhanVienResponse
    public AdminNhanVienResponse convertToResponse(NhanVien nhanVien) {
        AdminNhanVienResponse adminNhanVienResponse = new AdminNhanVienResponse();
        adminNhanVienResponse.setId(nhanVien.getId());
        adminNhanVienResponse.setMaNhanVien(nhanVien.getMaNhanVien());
        adminNhanVienResponse.setTenNhanVien(nhanVien.getTenNhanVien());
        adminNhanVienResponse.setTaiKhoan(nhanVien.getTaiKhoan());
//      adminNhanVienResponse.setMatKhau(nhanVien.getMatKhau());
        adminNhanVienResponse.setEmail(nhanVien.getEmail());
        adminNhanVienResponse.setSdt(nhanVien.getSdt());
        adminNhanVienResponse.setDiaChi(nhanVien.getDiaChi());
        adminNhanVienResponse.setTrangThai(nhanVien.getTrangThai());
        adminNhanVienResponse.setChucVu(nhanVien.getChucVu());
        adminNhanVienResponse.setGioiTinh(nhanVien.getGioiTinh());
        adminNhanVienResponse.setNamSinh(nhanVien.getNgaySinh());
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
        dto.setNamSinh(nv.getNgaySinh());
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
            if (adminStaffRequest.getMatKhau() != null && !adminStaffRequest.getMatKhau().isBlank()) {
                // Nếu mật khẩu mới khác mật khẩu hiện tại thì mã hóa và set
                if (!passwordEncoder.matches(adminStaffRequest.getMatKhau(), nhanVien.getMatKhau())) {
                    String encodedPassword = passwordEncoder.encode(adminStaffRequest.getMatKhau());
                    nhanVien.setMatKhau(encodedPassword);
                }
            }
        }
        nhanVien.setEmail(adminStaffRequest.getEmail());
        nhanVien.setSdt(adminStaffRequest.getSdt());
        nhanVien.setDiaChi(adminStaffRequest.getDiaChi());
        nhanVien.setTrangThai(adminStaffRequest.getTrangThai());
        nhanVien.setChucVu(adminStaffRequest.getChucVu());
        nhanVien.setGioiTinh(adminStaffRequest.getGioiTinh());
        nhanVien.setNgaySinh(adminStaffRequest.getNamSinh());
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
        return nhanVienRepository.findById(id).map(this::convertToResponse);
    }


    public AdminStaffRequest updateStaff(Integer id, AdminStaffRequest staffRequest) {
        NhanVien nhanvienExisting = nhanVienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với id: " + id));

        List<Map<String, String>> errors = new ArrayList<>();

        if (!Objects.equals(staffRequest.getTaiKhoan(), nhanvienExisting.getTaiKhoan()) &&
                (khachHangRepository.existsByTaiKhoan(staffRequest.getTaiKhoan()) ||
                        nhanVienRepository.existsByTaiKhoan(staffRequest.getTaiKhoan()))) {
            errors.add(Map.of("field", "taiKhoan", "message", "Tên tài khoản đã tồn tại!"));
        }

        if (!Objects.equals(staffRequest.getEmail(), nhanvienExisting.getEmail()) &&
                (khachHangRepository.existsByEmail(staffRequest.getEmail()) ||
                        nhanVienRepository.existsByEmail(staffRequest.getEmail()))) {
            errors.add(Map.of("field", "email", "message", "Email đã tồn tại!"));
        }

        if (!Objects.equals(staffRequest.getSdt(), nhanvienExisting.getSdt()) &&
                (khachHangRepository.existsBySdt(staffRequest.getSdt()) ||
                        nhanVienRepository.existsBySdt(staffRequest.getSdt()))) {
            errors.add(Map.of("field", "sdt", "message", "Số điện thoại đã tồn tại!"));
        }

        if (staffRequest.getMatKhau() != null && !staffRequest.getMatKhau().isBlank()) {
            String rawPassword = staffRequest.getMatKhau();
            if (rawPassword.length() < 6 || rawPassword.length() > 20) {
                errors.add(Map.of("field", "matKhau", "message", "Mật khẩu phải từ 6 đến 20 ký tự"));
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        convertNhanVienFromRequest(nhanvienExisting, staffRequest, false);
        NhanVien nhanVienUpdate = nhanVienRepository.save(nhanvienExisting);
        return convertToRequest(nhanVienUpdate);
    }

    public AdminStaffRequest createStaff(AdminStaffRequest staffRequest) {
        List<Map<String, String>> errors = new ArrayList<>();

        if (khachHangRepository.existsByTaiKhoan(staffRequest.getTaiKhoan()) || nhanVienRepository.existsByTaiKhoan(staffRequest.getTaiKhoan())) {
            errors.add(Map.of("field", "taiKhoan", "message", "Tên tài khoản đã tồn tại!"));
        }

        if (khachHangRepository.existsByEmail(staffRequest.getEmail()) || nhanVienRepository.existsByEmail(staffRequest.getEmail())) {
            errors.add(Map.of("field", "email", "message", "Email đã tồn tại!"));
        }

        if (khachHangRepository.existsBySdt(staffRequest.getSdt()) || nhanVienRepository.existsBySdt(staffRequest.getSdt())) {
            errors.add(Map.of("field", "sdt", "message", "Số điện thoại đã tồn tại!"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        NhanVien nhanVien = new NhanVien();
        convertNhanVienFromRequest(nhanVien, staffRequest, true);
        NhanVien nvAdd = nhanVienRepository.save(nhanVien);
        return convertToRequest(nvAdd);
    }


    //xoa nhan vien
    public void deleteStaff(Integer id) {
        if (!nhanVienRepository.existsById(id)) {
            throw new RuntimeException("Not found staff id: " + id);
        }
        nhanVienRepository.deleteById(id);
    }

}

