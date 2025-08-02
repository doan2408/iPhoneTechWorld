package org.example.websitetechworld.Services.ClientServices.TaiKhoanClientServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.ClientRequest.TaiKhoanClientRequest.ClientRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.TaiKhoanResponse.ClientResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;
import org.example.websitetechworld.Repository.DiaChiRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.NhanVienRepository;
import org.example.websitetechworld.Services.LoginServices.JwtService;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final PasswordEncoder passwordEncoder;
    private final KhachHangRepository khachHangRepository;
    private final NhanVienRepository nhanVienRepository;
    private final DiaChiRepository diaChiRepository;
    private final JwtService jwtService;

    //convert
    private ClientResponse convert(KhachHang khachHang) {
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(khachHang.getId());
        clientResponse.setMaKhachHang(khachHang.getMaKhachHang());
        clientResponse.setTenKhachHang(khachHang.getTenKhachHang());
        clientResponse.setSdt(khachHang.getSdt());
        clientResponse.setTaiKhoan(khachHang.getTaiKhoan());
        clientResponse.setMatKhau(khachHang.getMatKhau());
        clientResponse.setEmail(khachHang.getEmail());
        clientResponse.setNgaySinh(khachHang.getNgaySinh());
        clientResponse.setGioiTinh(khachHang.getGioiTinh());
        clientResponse.setAnh(khachHang.getAnh());
        clientResponse.setTrangThai(khachHang.getTrangThai());
        clientResponse.setHangKhachHang(
                khachHang.getHangThanhVien() != null ? khachHang.getHangThanhVien().getTenHang().getDisplayName() : null
        );

        // Lọc địa chỉ chính
        DiaChi diaChiChinh = khachHang.getDiaChis().stream()
                .filter(dc -> dc.getDiaChiChinh() == true) // Giả sử 1 là địa chỉ chính
                .findFirst() // Lấy địa chỉ chính đầu tiên nếu có
                .orElse(null); // Nếu không có địa chỉ chính, trả về null

        // Chỉ lấy địa chỉ chính nếu có
        if (diaChiChinh != null) {
            AdminDiaChiResponse diaChiResponse = new AdminDiaChiResponse();
            diaChiResponse.setId(diaChiChinh.getId());
            diaChiResponse.setTenKhachHang(diaChiChinh.getIdKhachHang().getTenKhachHang());
            diaChiResponse.setTenNguoiNhan(diaChiChinh.getTenNguoiNhan());
            diaChiResponse.setSoNha(diaChiChinh.getSoNha());
            diaChiResponse.setTenDuong(diaChiChinh.getTenDuong());
            diaChiResponse.setXaPhuong(diaChiChinh.getXaPhuong());
            diaChiResponse.setQuanHuyen(diaChiChinh.getQuanHuyen());
            diaChiResponse.setTinhThanhPho(diaChiChinh.getTinhThanhPho());
            diaChiResponse.setDiaChiChinh(diaChiChinh.getDiaChiChinh());

            clientResponse.setDiaChiChinh(diaChiResponse);
        }
        return clientResponse;
    }

    private KhachHang convertToKhachHang(ClientRequest request) {
        KhachHang khachHang = new KhachHang();
        khachHang.setId(request.getId());
        khachHang.setMaKhachHang(request.getMaKhachHang());
        khachHang.setTenKhachHang(request.getTenKhachHang());
        khachHang.setSdt(request.getSdt());
        khachHang.setTaiKhoan(request.getTaiKhoan());
        khachHang.setMatKhau(request.getMatKhau());
        khachHang.setEmail(request.getEmail());
        khachHang.setNgaySinh(request.getNgaySinh());
        khachHang.setGioiTinh(request.getGioiTinh());
        khachHang.setAnh(request.getAnh());
        khachHang.setTrangThai(request.getTrangThai());
        khachHang.setHangThanhVien(request.getHangKhachHang());
        return khachHang;
    }

    //SignUp, add mới
    public Map<String, Object> addClient(ClientRequest request) {
        List<Map<String, String>> errors = new ArrayList<>();
        // Check trùng tài khoản, email, sdt
        if(request.getTaiKhoan() != null) {
            if (khachHangRepository.existsByTaiKhoan(request.getTaiKhoan().trim()) || nhanVienRepository.existsByTaiKhoan(request.getTaiKhoan())) {
                errors.add(Map.of("field", "taiKhoan", "message", "Tên tài khoản đã tồn tại!"));
            }
        }

        if(request.getEmail() != null) {
            if (khachHangRepository.existsByEmail(request.getEmail().trim()) || nhanVienRepository.existsByEmail(request.getEmail())) {
                errors.add(Map.of("field", "email", "message", "Email đã tồn tại!"));
            }
        }

        if(request.getSdt() != null) {
            if (khachHangRepository.existsBySdt(request.getSdt().trim()) || nhanVienRepository.existsBySdt(request.getSdt())) {
                errors.add(Map.of("field", "sdt", "message", "Số điện thoại đã tồn tại!"));
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        String hashedPassword = passwordEncoder.encode(request.getMatKhau());
        request.setMatKhau(hashedPassword);

        request.setAnh("Default.jpg");
        request.setTrangThai(TrangThaiKhachHang.ACTIVE);

        request.setGioiTinh(true);
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
//        diaChi.setDiaChiChinh(true); // địa chỉ đầu tiên add : chính
//        diaChiRepository.save(diaChi);
        khachHangRepository.save(khachHangAdd);

        // ✅ Tạo token dựa trên KhachHang
        String accessToken = jwtService.generateAccessToken(khachHangAdd);
        String refreshToken = jwtService.generateRefreshToken(khachHangAdd);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "message", "Đăng ký thành công!",
                "roles", List.of(khachHangAdd.getRole()) // nếu có role
        );

    }

    public Optional<ClientResponse> hienThi(Integer id) {
        return khachHangRepository.findById(id).map(this::convert);
    }

    //update thong tin ca nhan (client)
    public KhachHang updateClient(Integer id, ClientRequest khachHangRequest) {
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
            existing.setTenKhachHang(khachHangRequest.getTenKhachHang());
            existing.setSdt(khachHangRequest.getSdt());
            existing.setEmail(khachHangRequest.getEmail());
            existing.setNgaySinh(khachHangRequest.getNgaySinh());
            existing.setGioiTinh(khachHangRequest.getGioiTinh());
            existing.setAnh(khachHangRequest.getAnh());

            if (khachHangRequest.getMatKhau() != null && !khachHangRequest.getMatKhau().isEmpty()) {
                String oldPassword = khachHangRequest.getMatKhauCu(); // cần thêm trường này trong ClientRequest

                if (!passwordEncoder.matches(oldPassword, existing.getMatKhau())) {
                    errors.add(Map.of("field", "matKhauCu", "message", "Mật khẩu cũ không đúng!"));
                }

                existing.setMatKhau(passwordEncoder.encode(khachHangRequest.getMatKhau()));
            }

            if(!errors.isEmpty()) {
                throw new ValidationException(errors);
            }

            return khachHangRepository.save(existing);
        }
        return null;
    }
}
