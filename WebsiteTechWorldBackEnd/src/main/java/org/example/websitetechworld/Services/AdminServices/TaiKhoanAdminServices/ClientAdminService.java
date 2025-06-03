package org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest.AdminClientRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminClientResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse;
import org.example.websitetechworld.Entity.DiaChi;
import org.example.websitetechworld.Entity.GioHang;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;
import org.example.websitetechworld.Repository.DiaChiRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.NhanVienRepository;
import org.hibernate.validator.internal.constraintvalidators.bv.NullValidator;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientAdminService {
    private final KhachHangRepository khachHangRepository;
    private final DiaChiRepository diaChiRepository;
    private final NhanVienRepository nhanVienRepository;
    private final PasswordEncoder passwordEncoder;

    ModelMapper modelMapper = new ModelMapper();


    //convert
    private AdminClientResponse convert(KhachHang khachHang) {
        AdminClientResponse adminClientResponse = new AdminClientResponse();
        adminClientResponse.setId(khachHang.getId());
        adminClientResponse.setMaKhachHang(khachHang.getMaKhachHang());
        adminClientResponse.setTenKhachHang(khachHang.getTenKhachHang());
        adminClientResponse.setSdt(khachHang.getSdt());
        adminClientResponse.setTaiKhoan(khachHang.getTaiKhoan());
        adminClientResponse.setMatKhau(khachHang.getMatKhau());
        adminClientResponse.setEmail(khachHang.getEmail());
        adminClientResponse.setNgaySinh(khachHang.getNgaySinh());
        adminClientResponse.setGioiTinh(khachHang.getGioiTinh());
        adminClientResponse.setAnh(khachHang.getAnh());
        adminClientResponse.setTongDiem(khachHang.getTongDiem());
        adminClientResponse.setSoDiemHienTai(khachHang.getSoDiemHienTai());
        adminClientResponse.setHangKhachHang(khachHang.getHangKhachHang().name());
        adminClientResponse.setTrangThai(khachHang.getTrangThai().name());

        // Tạo danh sách địa chỉ
        List<AdminDiaChiResponse> diaChiResponses = khachHang.getDiaChis().stream()
                .map(dc -> {
                    AdminDiaChiResponse diaChi = new AdminDiaChiResponse();
                    diaChi.setId(dc.getId());
                    diaChi.setTenKhachHang(dc.getIdKhachHang().getTenKhachHang());
                    diaChi.setTenNguoiNhan(dc.getTenNguoiNhan());
                    diaChi.setSdtNguoiNhan(dc.getSdtNguoiNhan());
                    diaChi.setSoNha(dc.getSoNha());
                    diaChi.setTenDuong(dc.getTenDuong());
                    diaChi.setXaPhuong(dc.getXaPhuong());
                    diaChi.setQuanHuyen(dc.getQuanHuyen());
                    diaChi.setTinhThanhPho(dc.getTinhThanhPho());
                    diaChi.setDiaChiChinh(dc.getDiaChiChinh());
                    return diaChi;
                })
                .collect(Collectors.toList());

        // Thêm danh sách địa chỉ vào response
        adminClientResponse.setDiaChi(diaChiResponses);

        return adminClientResponse;
    }

    private KhachHang convertToKhachHang(AdminClientRequest adminClientRequest) {
        KhachHang khachHang = new KhachHang();
        khachHang.setId(adminClientRequest.getId());
        khachHang.setMaKhachHang(adminClientRequest.getMaKhachHang());
        khachHang.setTenKhachHang(adminClientRequest.getTenKhachHang());
        khachHang.setSdt(adminClientRequest.getSdt());
        khachHang.setTaiKhoan(adminClientRequest.getTaiKhoan());
        khachHang.setMatKhau(adminClientRequest.getMatKhau());
        khachHang.setEmail(adminClientRequest.getEmail());
        khachHang.setNgaySinh(adminClientRequest.getNgaySinh());
        khachHang.setGioiTinh(adminClientRequest.getGioiTinh());
        khachHang.setAnh(adminClientRequest.getAnh());
        khachHang.setTongDiem(adminClientRequest.getTongDiem());
        khachHang.setSoDiemHienTai(adminClientRequest.getSoDiemHienTai());
        khachHang.setHangKhachHang(adminClientRequest.getHangKhachHang());
        khachHang.setTrangThai(adminClientRequest.getTrangThai());
     return khachHang;
    }


    //hien thi
    public Page<AdminClientResponse> getAllClient(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHang> clientPage = khachHangRepository.findAll(pageable);
        return clientPage.map(this::convert);
    }

    //detail client
    public Optional<AdminClientResponse> getClientById(Integer id) {
        return khachHangRepository.findById(id).map(this::convert);
    }

    //add client
    public KhachHang addClient(AdminClientRequest request) {
        // Check trùng tài khoản, email, sdt
        if (khachHangRepository.existsByTaiKhoan(request.getTaiKhoan()) || nhanVienRepository.existsByTaiKhoan(request.getTaiKhoan())) {
            throw new RuntimeException("Tên tài khoản đã tồn tại!");
        }
        if (khachHangRepository.existsByEmail(request.getEmail()) || nhanVienRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }
        if (khachHangRepository.existsBySdt(request.getSdt()) || nhanVienRepository.existsBySdt(request.getSdt())) {
            throw new RuntimeException("Số điện thoại đã tồn tại!");
        }

        String hashedPassword = passwordEncoder.encode(request.getMatKhau());
        request.setMatKhau(hashedPassword);

        request.setAnh("Default.jpg");
        request.setTrangThai(TrangThaiKhachHang.ACTIVE);
        request.setTongDiem(new BigDecimal(0));
        request.setSoDiemHienTai(new BigDecimal(0));
        request.setHangKhachHang(HangKhachHang.MEMBER);

        KhachHang khachHang = convertToKhachHang(request);

        KhachHang khachHangAdd = khachHangRepository.save(khachHang);

        //tạo giỏ hàng tương ứng với khách hàng mới vừa tạo
        GioHang gioHang = new GioHang();
        gioHang.setIdKhachHang(khachHangAdd); //oneToOne

        //set ngược lại
        khachHangAdd.setGioHang(gioHang);

        //lưu id khách hàng và giỏ hàng cũng sẽ được lưu theo Cascade
        return khachHangRepository.save(khachHangAdd);
    }

    //update khách hàng
    public KhachHang updateClient(Integer id, AdminClientRequest khachHangRequest) {
        KhachHang existing = khachHangRepository.findById(id).orElse(null);
        if (existing != null) {
            if (!existing.getTaiKhoan().equals(khachHangRequest.getTaiKhoan()) &&
                    (khachHangRepository.existsByTaiKhoan(khachHangRequest.getTaiKhoan()) || nhanVienRepository.existsByTaiKhoan(khachHangRequest.getTaiKhoan()))) {
                throw new RuntimeException("Tài khoản đã tồn tại!");
            }
            // Check trùng sdt, email (trừ chính bản thân khách hàng hiện tại)
            if (!existing.getSdt().equals(khachHangRequest.getSdt()) &&
                    (khachHangRepository.existsBySdt(khachHangRequest.getSdt()) || nhanVienRepository.existsBySdt(khachHangRequest.getSdt()))) {
                throw new RuntimeException("Số điện thoại đã tồn tại!");
            }
            if (!existing.getEmail().equals(khachHangRequest.getEmail()) &&
                    (khachHangRepository.existsByEmail(khachHangRequest.getEmail()) || nhanVienRepository.existsByEmail(khachHangRequest.getEmail()))) {
                throw new RuntimeException("Email đã tồn tại!");
            }

            existing.setTenKhachHang(khachHangRequest.getTenKhachHang());
            existing.setSdt(khachHangRequest.getSdt());
            existing.setEmail(khachHangRequest.getEmail());
            existing.setNgaySinh(khachHangRequest.getNgaySinh());
            existing.setGioiTinh(khachHangRequest.getGioiTinh());
            existing.setAnh(khachHangRequest.getAnh());

            if (khachHangRequest.getMatKhau() != null && !khachHangRequest.getMatKhau().isEmpty()) {
                if (!passwordEncoder.matches(khachHangRequest.getMatKhau(), existing.getMatKhau())) {
                    existing.setMatKhau(passwordEncoder.encode(khachHangRequest.getMatKhau()));
                }
            }
            return khachHangRepository.save(existing);
        }
        return null;
    }



}
