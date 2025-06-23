package org.example.websitetechworld.Controller.LoginController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.TokenRequest.UserLoginRequestDTO;
import org.example.websitetechworld.Dto.Request.TokenRequest.UserTokenRequestDTO;
import org.example.websitetechworld.Dto.Response.TokenResponse.UserTokenResponseDTO;
import org.example.websitetechworld.Entity.UserToken;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.example.websitetechworld.Repository.TokenService;
import org.example.websitetechworld.Services.LoginServices.UserTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    private final TokenService tokenService;
    private final UserTokenService userTokenService;


        //check role
        public boolean hasRole(Authentication auth, String role) {
            return auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals(role));
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO request) {
            List<Map<String, String>> errors = new ArrayList<>();

            try {
                // Kiểm tra tài khoản
                try {
                    userDetailsService.loadUserByUsername(request.getTaiKhoan());
                } catch (UsernameNotFoundException e) {
                    errors.add(Map.of("field", "tai_khoan", "message", "Tài khoản không tồn tại"));
                }

                // Kiểm tra mật khẩu (chỉ nếu tài khoản tồn tại)
                if (errors.isEmpty()) {
                    try {
                        Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getTaiKhoan(), request.getMatKhau())
                        );

                        CustomUserDetails taiKhoan = (CustomUserDetails) authentication.getPrincipal();


                        if(hasRole(authentication, "ROLE_STAFF") || hasRole(authentication, "ROLE_ADMIN")) {
                            if(!"ENABLE".equalsIgnoreCase(taiKhoan.getTrangThai())) {
                                System.out.println(taiKhoan.getTrangThai());
                                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                                        List.of(Map.of("field", "trang_thai", "message", "Tài khoản đã bị vô hiệu hóa"))
                                );
                            }
                        }
                        else {
                            if(!"ACTIVE".equalsIgnoreCase(taiKhoan.getTrangThai())) {
                                System.out.println(taiKhoan.getTrangThai());
                                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                                        List.of(Map.of("field", "trang_thai", "message", "Tài khoản đã bị vô hiệu hóa"))
                                );
                            }
                        }

                        //create access token + fresh token
                        String accessToken = tokenService.generateAccessToken(taiKhoan);
                        String refreshToken = tokenService.generateRefreshToken(taiKhoan);

                        userTokenService.saveTokens(taiKhoan, accessToken, refreshToken);

//                        String roles = authentication.getAuthorities().toString();

                        // ✅ Mới - đúng format JSON array
                        List<String> roles = authentication.getAuthorities()
                                .stream()
                                .map(authority -> authority.getAuthority())
                                .collect(Collectors.toList());
    //                  String roles = authentication.getAuthorities().iterator().next().getAuthority();
                        System.out.println(roles);
                        return ResponseEntity.ok(Map.of(
                                "message", "Đăng nhập thành công!",
                                "roles", roles,
                                "id", taiKhoan.getId(),
                                "fullName", taiKhoan.getFullName(),
                                "accessToken", accessToken,
                                "refreshToken", refreshToken,
                                "accessTokenExpiresAt", LocalDateTime.now().plusHours(2),
                                "refreshTokenExpiresAt", LocalDateTime.now().plusDays(7)
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
                    "email", userDetails.getAccount().getEmail(),
                    "sdt", userDetails.getAccount().getSdt(),
                    "id", userDetails.getId()
            ));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể lấy thông tin người dùng");
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody UserTokenRequestDTO request) {
        String refreshToken = request.getRefreshToken();

        //Tìm token trong DB
        Optional<UserToken> tokenOptional = userTokenService.findByToken(refreshToken);
        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Refresh token không hợp lệ hoặc không tồn tại"));
        }

        UserToken token = tokenOptional.get();

        //Lấy thông tin người dùng từ token
        CustomUserDetails userDetails = userTokenService.loadUserByToken(token);

        //Sinh access token mới
        String newAccessToken = tokenService.generateAccessToken(userDetails);

        UserTokenResponseDTO responseDTO = new UserTokenResponseDTO(
                newAccessToken,
                refreshToken, // dùng lại refresh token cũ
                Instant.now().plus(2, ChronoUnit.HOURS), // hoặc đọc thời gian từ JWT nếu bạn thích
                token.getExpiresAt()
        );

        return ResponseEntity.ok(responseDTO);
    }


}

