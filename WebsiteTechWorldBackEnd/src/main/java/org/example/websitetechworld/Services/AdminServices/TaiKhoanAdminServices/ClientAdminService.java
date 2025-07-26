package org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest.AdminClientRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminClientResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;
import org.example.websitetechworld.Repository.DiaChiRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.NhanVienRepository;
import org.example.websitetechworld.exception.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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


    //convertToResponse
    private AdminClientResponse convert(KhachHang khachHang) {
        AdminClientResponse adminClientResponse = new AdminClientResponse();
        adminClientResponse.setId(khachHang.getId());
        adminClientResponse.setMaKhachHang(khachHang.getMaKhachHang());
        adminClientResponse.setTenKhachHang(khachHang.getTenKhachHang());
        adminClientResponse.setSdt(khachHang.getSdt());
        adminClientResponse.setTaiKhoan(khachHang.getTaiKhoan());
//        adminClientResponse.setMatKhau(khachHang.getMatKhau());
        adminClientResponse.setEmail(khachHang.getEmail());
        adminClientResponse.setNgaySinh(khachHang.getNgaySinh());
        adminClientResponse.setGioiTinh(khachHang.getGioiTinh());
        adminClientResponse.setAnh(khachHang.getAnh());
        adminClientResponse.setTrangThai(khachHang.getTrangThai());
        adminClientResponse.setHangKhachHang(
                khachHang.getHangThanhVien() != null ? khachHang.getHangThanhVien().getTenHang().getDisplayName() : null
        );

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
        khachHang.setTrangThai(adminClientRequest.getTrangThai());
        khachHang.setHangThanhVien(adminClientRequest.getHangKhachHang());
     return khachHang;
    }


    //hien thi
    public Page<AdminClientResponse> getAllClient(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHang> clientPage = khachHangRepository.findAll(pageable);
        return clientPage.map(this::convert);
    }

    //search
    public Page<AdminClientResponse> getAllClient(int page, int size, String keyword, Boolean gioiTinh, TrangThaiKhachHang trangThai) {
        Pageable pageable = PageRequest.of(page, size);

        // Làm sạch keyword
        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) {
                keyword = null;
            }
        }

        boolean isFiltering =
                (keyword != null && !keyword.trim().isEmpty())
                        || gioiTinh != null || trangThai != null;

        Page<KhachHang> pageResult;

        if(isFiltering) {
            pageResult = khachHangRepository.findByFilters(keyword, gioiTinh, trangThai, pageable);
        }
        else {
            pageResult = khachHangRepository.findAll(pageable);
        }
        return pageResult.map(this::convert);
    }

    //detail client
    public Optional<AdminClientResponse> getClientById(Integer id) {
        return khachHangRepository.findById(id).map(this::convert);
    }

    //add client
    public KhachHang addClient(AdminClientRequest request) {
        List<Map<String, String>> errors = new ArrayList<>();
        // Check trùng tài khoản, email, sdt
        if(request.getTaiKhoan() != null && !request.getTaiKhoan().isEmpty()) {
            if (khachHangRepository.existsByTaiKhoan(request.getTaiKhoan().trim()) || nhanVienRepository.existsByTaiKhoan(request.getTaiKhoan().trim())) {
                errors.add(Map.of("field", "taiKhoan", "message", "Tên tài khoản đã tồn tại!"));
            }
        }

        if(request.getEmail() != null && !request.getEmail().isEmpty()) {
            if (khachHangRepository.existsByEmail(request.getEmail().trim()) || nhanVienRepository.existsByEmail(request.getEmail().trim())) {
                errors.add(Map.of("field", "email", "message", "Email đã tồn tại!"));
            }
        }

        if(request.getSdt() != null && !request.getSdt().isEmpty()) {
            if (khachHangRepository.existsBySdt(request.getSdt().trim()) || nhanVienRepository.existsBySdt(request.getSdt().trim())) {
                errors.add(Map.of("field", "sdt", "message", "Số điện thoại đã tồn tại!"));
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        if(request.getMatKhau() != null) {
            String hashedPassword = passwordEncoder.encode(request.getMatKhau().trim());
            request.setMatKhau(hashedPassword);
        }

        request.setAnh("Default.jpg");
        request.setTrangThai(TrangThaiKhachHang.ACTIVE);
        request.setTongDiem(new BigDecimal(0));
        request.setSoDiemHienTai(new BigDecimal(0));
        HangThanhVien hangThanhVien = new HangThanhVien();
        hangThanhVien.setId(1);
        request.setHangKhachHang(hangThanhVien);

        KhachHang khachHang = convertToKhachHang(request);

        KhachHang khachHangAdd = khachHangRepository.save(khachHang);

        //tạo giỏ hàng tương ứng với khách hàng mới vừa tạo
        GioHang gioHang = new GioHang();
        gioHang.setIdKhachHang(khachHangAdd); //oneToOne

        //tạo ví tương ứng với khách hàng mới vừa tạo
        ViDiem viDiem = new ViDiem();
        viDiem.setKhachHang(khachHangAdd);
        viDiem.setDiemKhaDung(new BigDecimal(0));

        //set ngược lại one to one
        khachHangAdd.setGioHang(gioHang);
        khachHangAdd.setViDiem(viDiem);

//        DiaChi diaChi = new DiaChi();
//        diaChi.setIdKhachHang(khachHangAdd);
//        diaChi.setDiaChiChinh(true);
//        diaChiRepository.save(diaChi);
        //lưu id khách hàng và giỏ hàng, ví cũng sẽ được lưu theo Cascade
        return khachHangRepository.save(khachHangAdd);
    }

    //update khách hàng
    public KhachHang updateClient(Integer id, AdminClientRequest khachHangRequest) {
        KhachHang existing = khachHangRepository.findById(id).orElse(null);

        List<Map<String, String>> errors = new ArrayList<>();
        if (existing != null) {
            // Kiểm tra tài khoản có bị trùng không (trừ chính nó)
            if (khachHangRequest.getTaiKhoan() != null &&
                    !khachHangRequest.getTaiKhoan().equals(existing.getTaiKhoan()) &&
                    (khachHangRepository.existsByTaiKhoan(khachHangRequest.getTaiKhoan()) ||
                            nhanVienRepository.existsByTaiKhoan(khachHangRequest.getTaiKhoan()))) {

                errors.add(Map.of("field", "taiKhoan", "message", "Tên tài khoản đã tồn tại!"));
            }

            // Kiểm tra trùng số điện thoại
            if (khachHangRequest.getSdt() != null &&
                    !khachHangRequest.getSdt().equals(existing.getSdt()) &&
                    (khachHangRepository.existsBySdt(khachHangRequest.getSdt()) ||
                            nhanVienRepository.existsBySdt(khachHangRequest.getSdt()))) {

                errors.add(Map.of("field", "sdt", "message", "Số điện thoại đã tồn tại!"));
            }

            // Kiểm tra trùng email
            if (khachHangRequest.getEmail() != null &&
                    !khachHangRequest.getEmail().equals(existing.getEmail()) &&
                    (khachHangRepository.existsByEmail(khachHangRequest.getEmail()) ||
                            nhanVienRepository.existsByEmail(khachHangRequest.getEmail()))) {

                errors.add(Map.of("field", "email", "message", "Email đã tồn tại!"));
            }

            if(!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
            existing.setTaiKhoan(khachHangRequest.getTaiKhoan());
            existing.setTenKhachHang(khachHangRequest.getTenKhachHang());
            existing.setSdt(khachHangRequest.getSdt());
            existing.setEmail(khachHangRequest.getEmail());
            existing.setNgaySinh(khachHangRequest.getNgaySinh());
            existing.setGioiTinh(khachHangRequest.getGioiTinh());
            existing.setAnh(khachHangRequest.getAnh());

            if (khachHangRequest.getMatKhau() != null && !khachHangRequest.getMatKhau().trim().isEmpty()) {
                if (!passwordEncoder.matches(khachHangRequest.getMatKhau().trim(), existing.getMatKhau().trim())) {
                    existing.setMatKhau(passwordEncoder.encode(khachHangRequest.getMatKhau().trim()));
                }
            }
            return khachHangRepository.save(existing);
        }
        return null;
    }
}
