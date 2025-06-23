package org.example.websitetechworld.Controller.LoginController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.TokenRequest.UserLoginRequestDTO;
import org.example.websitetechworld.Dto.Request.TokenRequest.UserTokenRequestDTO;
import org.example.websitetechworld.Dto.Response.TokenResponse.UserTokenResponseDTO;
import org.example.websitetechworld.Entity.UserToken;
import org.example.websitetechworld.Repository.UserTokenRepository;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.example.websitetechworld.Repository.TokenService;
import org.example.websitetechworld.Repository.UserTokenService;
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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    private final TokenService tokenService;
    private final UserTokenService userTokenService;
    private final UserTokenRepository userTokenRepository;


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

                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        CustomUserDetails taiKhoan = (CustomUserDetails) authentication.getPrincipal();


                        if(hasRole(authentication, "ROLE_STAFF") || hasRole(authentication, "ROLE_ADMIN")) {
                            if(!"ENABLE".equalsIgnoreCase(taiKhoan.getTrangThai())) {
                                System.out.println(taiKhoan.getTrangThai());
                                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                                        List.of(Map.of("field", "trang_thai", "message", "Tài khoản đã bị vô hiệu hóa"))
                                );
                            }
                        }
                        else {
                            if(!"ACTIVE".equalsIgnoreCase(taiKhoan.getTrangThai())) {
                                System.out.println(taiKhoan.getTrangThai());
                                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
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
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
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
        Optional<UserToken> tokenOptional = userTokenService.findByToken(refreshToken);
        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Refresh token không hợp lệ hoặc không tồn tại"));
        }
        UserToken token = tokenOptional.get();
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("techworld1234567890techworld1234567890")
                    .parseClaimsJws(refreshToken)
                    .getBody();
            if (!"refresh".equals(claims.get("type"))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Token không phải refresh token"));
            }
            if (claims.getExpiration().before(new Date())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Refresh token đã hết hạn"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Refresh token không hợp lệ", "message", e.getMessage()));
        }
        CustomUserDetails userDetails = userTokenService.loadUserByToken(token);
        String newAccessToken = tokenService.generateAccessToken(userDetails);
        UserTokenResponseDTO responseDTO = new UserTokenResponseDTO(
                newAccessToken,
                refreshToken,
                Instant.now().plus(2, ChronoUnit.HOURS),
                token.getExpiresAt()
        );
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody UserTokenRequestDTO request) {
        String refreshToken = request.getRefreshToken();

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Refresh token không được để trống"));
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("techworld1234567890techworld1234567890")
                    .parseClaimsJws(refreshToken)
                    .getBody();

            if (!"refresh".equals(claims.get("type"))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Token không phải refresh token"));
            }

            Optional<UserToken> tokenOptional = userTokenService.findByToken(refreshToken);

            if (tokenOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Refresh token không tồn tại trong hệ thống"));
            }

            userTokenRepository.delete(tokenOptional.get());

            return ResponseEntity.ok(Map.of("message", "Đăng xuất thành công, token đã bị xoá"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Token không hợp lệ", "error", e.getMessage()));
        }
    }

}

