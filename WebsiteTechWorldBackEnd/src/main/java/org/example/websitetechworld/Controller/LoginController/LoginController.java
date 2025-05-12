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
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class LoginController {
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        try {
            String username = request.get("tai_khoan");
            String password = request.get("mat_khau");


            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // ✅ Gán vào SecurityContext
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);

            // ✅ Lưu context vào session
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

            String roles = authentication.getAuthorities().toString();


//            authentication.getPrincipal() trả về UserDetails mà loadUserByName retunrn về CustomUserDetails được implement UserDetails
            CustomUserDetails taiKhoan = (CustomUserDetails) authentication.getPrincipal();
            System.out.println(taiKhoan.getId());

            return ResponseEntity.ok(Map.of(
                    "message", "Đăng nhập thành công!",
                    "roles", roles,
                    "id", taiKhoan.getId()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tài khoản hoặc mật khẩu!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server.");
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

        if (principal instanceof UserDetails userDetails) {
            return ResponseEntity.ok(Map.of(
                    "tai_khoan", userDetails.getUsername(),
                    "roles", userDetails.getAuthorities()
            ));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể lấy thông tin người dùng");
    }

}

