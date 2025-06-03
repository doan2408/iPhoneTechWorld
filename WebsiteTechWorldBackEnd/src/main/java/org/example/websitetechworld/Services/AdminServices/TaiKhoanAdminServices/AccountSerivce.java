package org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices;

import com.cloudinary.provisioning.Account;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest.JointAccountDto;
import org.example.websitetechworld.Entity.GioHang;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.NhanVien;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.NhanVienRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountSerivce {
    private final NhanVienRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveOrUpdateNhanVien(JointAccountDto dto) {
        boolean isUpdate = dto.getId() != null;
        NhanVien oldEntity = isUpdate ? nhanVienRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên")) : null;

        // Check trùng tên tài khoản
        if (!isUpdate || !dto.getTaiKhoan().equals(oldEntity.getTaiKhoan())) {
            if (nhanVienRepository.existsByTaiKhoanAndIdNot(dto.getTaiKhoan(), dto.getId()) ||
                    khachHangRepository.existsByTaiKhoan(dto.getTaiKhoan())) {
                throw new RuntimeException("Tên tài khoản đã tồn tại!");
            }
        }
        // Check email
        if (!isUpdate || !dto.getEmail().equals(oldEntity.getEmail())) {
            if (nhanVienRepository.existsByEmailAndIdNot(dto.getEmail(), dto.getId()) ||
                    khachHangRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Email đã tồn tại!");
            }
        }
        // Check sdt
        if (!isUpdate || !dto.getSdt().equals(oldEntity.getSdt())) {
            if (nhanVienRepository.existsBySdtAndIdNot(dto.getSdt(), dto.getId()) ||
                    khachHangRepository.existsBySdt(dto.getSdt())) {
                throw new RuntimeException("SĐT đã tồn tại!");
            }
        }

        NhanVien entity = isUpdate ? oldEntity : new NhanVien();
        entity.setTenNhanVien(dto.getTen());
        entity.setTaiKhoan(dto.getTaiKhoan());
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        entity.setMatKhau(encodedPassword);
        entity.setEmail(dto.getEmail());
        entity.setSdt(dto.getSdt());
        entity.setDiaChi(dto.getDiaChi());
        entity.setTrangThai(dto.getTrangThaiNhanVien());
        entity.setChucVu(dto.getRole());
        entity.setGioiTinh(dto.isGioiTinh());
        entity.setNgaySinh(dto.getNgaySinh());
        nhanVienRepository.save(entity);
    }


    public void saveOrUpdateKhachHang(JointAccountDto dto) {
        boolean isUpdate = dto.getId() != null;

        KhachHang oldEntity = isUpdate
                ? khachHangRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"))
                : null;

        // Check trùng tài khoản
        if (!isUpdate || !dto.getTaiKhoan().equals(oldEntity.getTaiKhoan())) {
            if (khachHangRepository.existsByTaiKhoanAndIdNot(dto.getTaiKhoan(), dto.getId()) ||
                    nhanVienRepository.existsByTaiKhoan(dto.getTaiKhoan())) {
                throw new RuntimeException("Tên tài khoản đã tồn tại!");
            }
        }

        // Check trùng email
        if (!isUpdate || !dto.getEmail().equals(oldEntity.getEmail())) {
            if (khachHangRepository.existsByEmailAndIdNot(dto.getEmail(), dto.getId()) ||
                    nhanVienRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Email đã tồn tại!");
            }
        }

        // Check trùng SĐT
        if (!isUpdate || !dto.getSdt().equals(oldEntity.getSdt())) {
            if (khachHangRepository.existsBySdtAndIdNot(dto.getSdt(), dto.getId()) ||
                    nhanVienRepository.existsBySdt(dto.getSdt())) {
                throw new RuntimeException("SĐT đã tồn tại!");
            }
        }

        KhachHang entity = isUpdate ? oldEntity : new KhachHang();

        // Cập nhật thông tin chung
        entity.setTaiKhoan(dto.getTaiKhoan());
        entity.setTenKhachHang(dto.getTen());
        entity.setEmail(dto.getEmail());
        entity.setSdt(dto.getSdt());
        entity.setNgaySinh(dto.getNgaySinh());
        entity.setGioiTinh(dto.isGioiTinh());
        entity.setAnh(dto.getAnh());

        // Mã hóa mật khẩu nếu thêm mới hoặc mật khẩu mới được gửi lên
        if (!isUpdate || (dto.getPassword() != null && !dto.getPassword().isEmpty())) {
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            entity.setMatKhau(encodedPassword);
        }

        // Nếu là thêm mới thì set điểm mặc định và hạng mặc định
        if (!isUpdate) {
            entity.setTongDiem(new BigDecimal(0));
            entity.setSoDiemHienTai(new BigDecimal(0));
            entity.setHangKhachHang(HangKhachHang.MEMBER);

            // Lưu Khách hàng trước để có ID cho giỏ hàng
            KhachHang savedEntity = khachHangRepository.save(entity);

            GioHang gioHang = new GioHang();
            gioHang.setIdKhachHang(savedEntity); // liên kết oneToOne

            savedEntity.setGioHang(gioHang);

            // Lưu lại để lưu giỏ hàng theo cascade
            khachHangRepository.save(savedEntity);
        }
        else {
            // Nếu update thì chỉ cần save thông tin cập nhật
            khachHangRepository.save(entity);
        }
    }

 }
