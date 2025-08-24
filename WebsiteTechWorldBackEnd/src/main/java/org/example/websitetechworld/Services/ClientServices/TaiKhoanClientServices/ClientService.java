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
import org.example.websitetechworld.Services.LoginServices.ForgotPasswordService;
import org.example.websitetechworld.Services.LoginServices.JwtService;
import org.example.websitetechworld.Services.LoginServices.MailService;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final PasswordEncoder passwordEncoder;
    private final KhachHangRepository khachHangRepository;
    private final NhanVienRepository nhanVienRepository;
    private final JwtService jwtService;

    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, ClientRequest> tempRegistrationStorage = new ConcurrentHashMap<>();

    private static final SecureRandom random = new SecureRandom();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final MailService mailService;

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
            diaChiResponse.setEmailNguoiNhan(diaChiChinh.getEmailNguoiNhan());
            diaChiResponse.setTinhThanhPho(diaChiChinh.getTinhThanhPho());
            diaChiResponse.setDiaChiChinh(diaChiChinh.getDiaChiChinh());

            clientResponse.setDiaChiChinh(diaChiResponse);
        }
        return clientResponse;
    }

    private KhachHang convertToKhachHang(ClientRequest request) {
        KhachHang khachHang = new KhachHang();
        khachHang.setTenKhachHang(request.getTenKhachHang());
        khachHang.setSdt(request.getSdt());
        khachHang.setTaiKhoan(request.getTaiKhoan());
        khachHang.setMatKhau(request.getMatKhau());
        khachHang.setEmail(request.getEmail());
        khachHang.setNgaySinh(request.getNgaySinh());
        khachHang.setGioiTinh(request.getGioiTinh());
        khachHang.setAnh(request.getAnh());
        khachHang.setTrangThai(request.getTrangThai());
//        khachHang.setHangThanhVien(request.getHangKhachHang());
        return khachHang;
    }

    // Send verify code
    public Map<String, Object> verifyRegister(ClientRequest request) {
        List<Map<String, String>> errors = new ArrayList<>();
        // Check trùng tài khoản, email, sdt
        if (request.getTaiKhoan() != null) {
            if (khachHangRepository.existsByTaiKhoan(request.getTaiKhoan().trim()) || nhanVienRepository.existsByTaiKhoan(request.getTaiKhoan())) {
                errors.add(Map.of("field", "taiKhoan", "message", "Tên tài khoản đã tồn tại!"));
            }
        }

        // nếu là email nhân viên thì k cho dky
        if (nhanVienRepository.existsByEmail(request.getEmail())) {
            errors.add(Map.of("field", "emailExist", "message", "Email đã tồn tại"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        // In case the direct customer has purchased the product but does not have an account
        if (khachHangRepository.existsByEmail(request.getEmail())) {
            Optional<KhachHang> khachHangOptional = khachHangRepository.findByEmail(request.getEmail());
            if (khachHangOptional.isPresent()) {
                KhachHang khachHangExist = khachHangOptional.get();
                if (khachHangExist.getTaiKhoan() != null) {
                    errors.add(Map.of("field", "emailExist", "message", "Email đã tồn tại"));
                } else {
                    // send mail for guest has purchased the product
                    boolean emailSent = sendRegistrationVerificationEmail(request.getEmail());

                    if (emailSent) {
                        tempRegistrationStorage.put(request.getEmail(), request);
                        return Map.of(
                                "field", true,
                                "message", "Mã xác nhận đã được gửi đến email của bạn. Vui lòng kiểm tra email và nhập mã xác nhận",
                                "email", request.getEmail()
                        );
                    } else {
                        errors.add(Map.of("field", "email", "message", "Không thể gửi email xác nhận"));
                        throw new ValidationException(errors);
                    }
                }
            }
        }

        if (request.getSdt() != null) {
            if (khachHangRepository.existsBySdt(request.getSdt().trim()) || nhanVienRepository.existsBySdt(request.getSdt())) {
                errors.add(Map.of("field", "sdt", "message", "Số điện thoại đã tồn tại!"));
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        boolean emailSent = sendRegistrationVerificationEmail(request.getEmail());
        if (emailSent) {
            tempRegistrationStorage.put(request.getEmail(), request);
            // store registration data temporarily
            return Map.of(
                    "requireVerification", true,
                    "message", "Mã xác nhận đã được gửi đến email của bạn. Vui lòng kiểm tra email và nhập mã xác nhận.",
                    "email", request.getEmail()
            );
        } else {
            errors.add(Map.of("field", "email", "message", "Không thể gửi email xác nhận"));
            throw new ValidationException(errors);
        }
    }

    public Map<String, Object> completeRegistration(String email, String verificationCode) {
        List<Map<String, String>> errors = new ArrayList<>();

        System.out.println("email nhan duoc: "+ email);
        System.out.println("code nhan duoc: "+ verificationCode);
        System.out.println("verifycode: " + verifyCode(email, verificationCode));
        // Verify the code first
        if (!verifyCode(email, verificationCode)) {
            errors.add(Map.of("field", "code", "message", "Mã xác nhận không đúng hoặc đã hết hạn"));
            throw new ValidationException(errors);
        }

        // Get temporarily stored registration data
        ClientRequest request = tempRegistrationStorage.get(email);
        if (request == null) {
            errors.add(Map.of("field", "session", "message", "Phiên đăng ký đã hết hạn. Vui lòng thử lại"));
            throw new ValidationException(errors);
        }

        try {
            // Check if this is updating an existing guest or creating new customer
            Optional<KhachHang> khachHangOptional = khachHangRepository.findByEmail(email);

            if (khachHangOptional.isPresent()) {
                KhachHang khachHangExist = khachHangOptional.get();
                if (khachHangExist.getTaiKhoan() == null) {
                    khachHangExist.setTaiKhoan(request.getTaiKhoan());
                    // Update existing guest customer
                    String hashedPassword = passwordEncoder.encode(request.getMatKhau());
                    khachHangExist.setMatKhau(hashedPassword);
                    khachHangExist.setTenKhachHang(request.getTenKhachHang());
                    khachHangExist.setAnh("Default.jpg");
                    khachHangExist.setTrangThai(TrangThaiKhachHang.ACTIVE);
                    khachHangExist.setGioiTinh(true);
                    khachHangRepository.save(khachHangExist);

                    // Clean up
                    removeCode(email);
                    tempRegistrationStorage.remove(email);

                    // ✅ Tạo token dựa trên KhachHang đã update
                    String accessToken = jwtService.generateAccessToken(khachHangExist);
                    String refreshToken = jwtService.generateRefreshToken(khachHangExist);

                    return Map.of(
                            "accessToken", accessToken,
                            "refreshToken", refreshToken,
                            "message", "Tài khoản đã được tạo thành công!",
                            "roles", List.of(khachHangExist.getRole())
                    );

                } else {
                    errors.add(Map.of("field", "emailExist", "message", "Email này đã được đăng ký tài khoản"));
                    throw new ValidationException(errors);
                }
            } else {
                // Create completely new customer
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
                gioHang.setIdKhachHang(khachHangAdd);

                //tạo ví tương ứng với khách hàng mới vừa tạo
                ViDiem viDiem = new ViDiem();
                viDiem.setKhachHang(khachHangAdd);
                viDiem.setDiemKhaDung(new BigDecimal(0));

                //set ngược lại one to one
                khachHangAdd.setGioHang(gioHang);
                khachHangAdd.setViDiem(viDiem);

                khachHangRepository.save(khachHangAdd);

                // Clean up
                removeCode(email);
                tempRegistrationStorage.remove(email);

                // ✅ Tạo token dựa trên KhachHang mới
                String accessToken = jwtService.generateAccessToken(khachHangAdd);
                String refreshToken = jwtService.generateRefreshToken(khachHangAdd);

                return Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken,
                        "message", "Đăng ký thành công!",
                        "roles", List.of(khachHangAdd.getRole())
                );
            }

        } catch (Exception e) {
            errors.add(Map.of("field", "system", "message", "Có lỗi xảy ra: " + e.getMessage()));
            throw new ValidationException(errors);
        }
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
                String oldPassword = khachHangRequest.getMatKhauCu();

                if (!passwordEncoder.matches(oldPassword, existing.getMatKhau())) {
                    errors.add(Map.of("field", "matKhauCu", "message", "Mật khẩu cũ không đúng!"));
                }

                existing.setMatKhau(passwordEncoder.encode(khachHangRequest.getMatKhau()));
            }

            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }

            return khachHangRepository.save(existing);
        }
        return null;
    }

    // Tạo mã random
    private String generateCode(int length) {
        // Sử dụng cả chữ và số để tăng độ bảo mật
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }

    public boolean sendRegistrationVerificationEmail(String email) {
        try {
            String verificationCode = generateCode(8); // Tăng lên 8 ký tự
            verificationCodes.put(email, verificationCode);

            // Tăng thời gian expire lên 5 phút
            scheduler.schedule(() -> {
                verificationCodes.remove(email);
            }, 5, TimeUnit.MINUTES);

            String subject = "Xác nhận tài khoản - Tech World";
            String body = "Chào bạn,\n\n" +
                    "Bạn đang tạo tài khoản tại Tech World.\n <br>" +
                    "Mã xác nhận của bạn là: " + verificationCode + "\n\n <br>" +
                    "Mã này có hiệu lực trong vòng 5 phút.\n\n <br>" +
                    "Nếu bạn không thực hiện hành động này, vui lòng bỏ qua email này.\n\n <br>" +
                    "Trân trọng,\n <br>" +
                    "Tech World Shop";

            mailService.sendMail(email, subject, body);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    // Bạn có thể thêm các phương thức kiểm tra mã, xóa mã nếu muốn
    public boolean verifyCode(String email, String code) {
        String savedCode = verificationCodes.get(email);
        return savedCode != null && savedCode.equals(code);
    }

    public void removeCode(String email) {
        verificationCodes.remove(email);
    }
}
