package org.example.websitetechworld.Controller.LoginController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest.AdminClientRequest;
import org.example.websitetechworld.Dto.Request.ClientRequest.TaiKhoanClientRequest.ClientRequest;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Services.ClientServices.TaiKhoanClientServices.ClientService;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SignUpController {
    private final ClientService clientService;

    //dang ki tai khoan (Client)
    @PostMapping("/register/verify")
    public ResponseEntity<?> registerVerify(@RequestBody @Valid ClientRequest khachHang, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok(clientService.verifyRegister(khachHang));
        }
        catch (ValidationException e) {
            // Bắt riêng FieldException trả lỗi với field cụ thể
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrors());
        }
        catch (IllegalArgumentException e) {
            // Trả về lỗi với field = "other" để frontend biết là lỗi chung
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    List.of(Map.of("field", "other", "message", e.getMessage()))
            );
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    List.of(Map.of("field", "other", "message", "Lỗi hệ thống: " + e.getMessage()))
            );
        }
    }

    // Xác nhận mã và hoàn tất đăng ký
    @PostMapping("/register/complete")
    public ResponseEntity<?> completeRegistration(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("email");
        String verificationCode = requestData.get("verificationCode");

        // Validate input
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    List.of(Map.of("field", "email", "message", "Email không được để trống"))
            );
        }

        if (verificationCode == null || verificationCode.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    List.of(Map.of("field", "verificationCode", "message", "Mã xác nhận không được để trống"))
            );
        }

        try {
            Map<String, Object> result = clientService.completeRegistration(email, verificationCode);
            return ResponseEntity.ok(result);
        }
        catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrors());
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    List.of(Map.of("field", "other", "message", e.getMessage()))
            );
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    List.of(Map.of("field", "other", "message", "Lỗi hệ thống: " + e.getMessage()))
            );
        }
    }

    // API để gửi lại mã xác nhận
    @PostMapping("/register/resend-code")
    public ResponseEntity<?> resendVerificationCode(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("email");

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    List.of(Map.of("field", "email", "message", "Email không được để trống"))
            );
        }

        try {
            boolean sent = clientService.sendRegistrationVerificationEmail(email);
            if (sent) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Mã xác nhận đã được gửi lại đến email của bạn"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        List.of(Map.of("field", "email", "message", "Không thể gửi lại mã xác nhận"))
                );
            }
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    List.of(Map.of("field", "other", "message", "Lỗi hệ thống: " + e.getMessage()))
            );
        }
    }


}
