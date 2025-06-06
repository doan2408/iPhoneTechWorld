package org.example.websitetechworld.Controller.LoginController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Repository.JointAccount;
import org.example.websitetechworld.Services.LoginServices.ForgotPasswordService;
import org.example.websitetechworld.Services.LoginServices.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    //check email trong database -> sendMail
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        boolean success = forgotPasswordService.sendForgotPasswordEmail(email);

        if (success) {
            return ResponseEntity.ok("Vui lòng kiểm tra email, mã xác nhận có hiệu lực trong vòng 1 phút");
        } else {
            return ResponseEntity.badRequest().body("Email không tồn tại trong hệ thống.");
        }
    }


    // Xác thực mã code gửi về email
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String code = payload.get("code");

        if (email == null || code == null || email.isEmpty() || code.isEmpty()) {
            return ResponseEntity.badRequest().body("Email và mã xác nhận không được để trống");
        }

        boolean valid = forgotPasswordService.verifyCode(email, code);
        if (valid) {
            return ResponseEntity.ok("Mã xác nhận hợp lệ.");
        } else {
            return ResponseEntity.badRequest().body("Mã xác nhận không đúng hoặc đã hết hạn.");
        }
    }

    // Đặt lại mật khẩu mới
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String newPassword = payload.get("newPassword");


        if (email == null  || newPassword == null
                || email.isEmpty() || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("Email và mật khẩu mới không được để trống");
        }

        // Cập nhật mật khẩu mới
        boolean updated = forgotPasswordService.updatePassword(email, newPassword);

        if (updated) {
            forgotPasswordService.removeCode(email); // Xóa mã sau khi đặt lại thành công
            return ResponseEntity.ok("Đặt lại mật khẩu thành công");
        } else {
            return ResponseEntity.badRequest().body("Không thể cập nhật mật khẩu. Email không tồn tại.");
        }
    }

}
