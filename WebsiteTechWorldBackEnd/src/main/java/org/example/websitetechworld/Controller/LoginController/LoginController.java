package org.example.websitetechworld.Controller.LoginController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Repository.JointAccount;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        try {
            String username = request.get("tai_khoan");
             String password = request.get("mat_khau");

            List<Map<String, String>> errors = new ArrayList<>();

            // Kiểm tra tài khoản
            try {
                userDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                errors.add(Map.of("field", "tai_khoan", "message", "Tài khoản không tồn tại"));
            }

            // Kiểm tra mật khẩu (chỉ nếu tài khoản tồn tại)
            if (errors.isEmpty()) {
                try {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(username, password)
                    );

                    SecurityContext context = SecurityContextHolder.getContext();
                    context.setAuthentication(authentication);

                    HttpSession session = httpRequest.getSession(true);
                    session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

                    String roles = authentication.getAuthorities().toString();
                    CustomUserDetails taiKhoan = (CustomUserDetails) authentication.getPrincipal();

                    return ResponseEntity.ok(Map.of(
                            "message", "Đăng nhập thành công!",
                            "roles", roles,
                            "id", taiKhoan.getId(),
                            "fullName", taiKhoan.getFullName()
                    ));
                } catch (BadCredentialsException e) {
                    errors.add(Map.of("field", "mat_khau", "message", "Mật khẩu không đúng"));
                }
            }

            // Trả về mảng lỗi nếu có
            if (!errors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
            }

            // Trường hợp không có lỗi nhưng cũng không xác thực được (hiếm)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of(Map.of("field", "server", "message", "Lỗi xử lý đăng nhập")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of(Map.of("field", "server", "message", "Lỗi server: " + e.getMessage())));
        }
    }


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }

        Object principal = authentication.getPrincipal(); // là CustomUserDetails

        if (principal instanceof CustomUserDetails userDetails) {
            return ResponseEntity.ok(Map.of(
                    "tai_khoan", userDetails.getUsername(),
                    "roles", userDetails.getAuthorities(),
                    "fullName", userDetails.getFullName(),
                    "id", userDetails.getId()
            ));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể lấy thông tin người dùng");
    }
}

