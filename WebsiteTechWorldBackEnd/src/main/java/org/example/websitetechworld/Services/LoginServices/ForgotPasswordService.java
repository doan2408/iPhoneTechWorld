package org.example.websitetechworld.Services.LoginServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Repository.JointAccount;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.NhanVienRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserService userService;
    private final MailService mailService;

    private final NhanVienRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;


    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private static final SecureRandom random = new SecureRandom();
    private static final String DIGITS = "0123456789";
    private final PasswordEncoder passwordEncoder;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    // Tạo mã random 6 số
    private String generateCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++) {
            int idx = random.nextInt(DIGITS.length());
            sb.append(DIGITS.charAt(idx));
        }
        return sb.toString();
    }


    public boolean sendForgotPasswordEmail(String email) {
        Optional<JointAccount> accountOptional = userService.findByEmail(email);

        if (accountOptional.isEmpty()) return false;

        JointAccount account = accountOptional.get();


        String verificationCode = generateCode(6);
        verificationCodes.put(email, verificationCode);

        // Lên lịch xóa mã sau 1 phút (60 giây)
        scheduler.schedule(() -> {
            verificationCodes.remove(email);
        }, 1, TimeUnit.MINUTES);

        // Gửi mail chứa đường dẫn hoặc mã xác nhận reset mật khẩu
        String subject = "Yêu cầu đặt lại mật khẩu";
        String body = "<p>Xin chào <b>" + account.getFullName() + "</b>,</p>" +
                "<p>Bạn đã yêu cầu đặt lại mật khẩu. Nhấn vào liên kết bên dưới để tiếp tục:</p>" +
                "<p>" + verificationCode + "</p>" +
                "<a href='http://localhost:5173/reset-password?email="
                + account.getEmail() + "&code=" +verificationCode +" '>Đặt lại mật khẩu</a>" +
                "<p>Nếu bạn không yêu cầu, hãy bỏ qua email này.</p>";

        mailService.sendMail(email, subject, body);
        return true;
    }

    @Transactional
    public boolean updatePassword(String email, String rawPassword) {
        String hashedPassword = passwordEncoder.encode(rawPassword);

        if (nhanVienRepository.existsByEmail(email)) {
            nhanVienRepository.updatePasswordByEmail(email, hashedPassword);
            return true;
        } else if (khachHangRepository.existsByEmail(email)) {
            khachHangRepository.updatePasswordByEmail(email, hashedPassword);
            return true;
        }
        return false; // email không tồn tại trong cả hai bảng
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
