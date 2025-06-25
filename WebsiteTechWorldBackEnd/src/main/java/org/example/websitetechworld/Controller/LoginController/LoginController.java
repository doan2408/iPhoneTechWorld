package org.example.websitetechworld.Controller.LoginController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.TokenRequest.UserLoginRequestDTO;
import org.example.websitetechworld.Dto.Request.TokenRequest.UserTokenRequestDTO;
import org.example.websitetechworld.Dto.Response.TokenResponse.UserTokenResponseDTO;
import org.example.websitetechworld.Entity.UserToken;
import org.example.websitetechworld.Repository.UserTokenRepository;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.example.websitetechworld.Repository.TokenService;
import org.example.websitetechworld.Repository.UserTokenService;
import org.example.websitetechworld.Services.LoginServices.JwtService;
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

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
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
    private final JwtService jwtService;


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

                        //đúng format JSON array
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
            Claims claims = jwtService.extractAllClaims(refreshToken);

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
        System.out.println("🔍 Logout request - Refresh token: " + (refreshToken != null ? refreshToken.substring(0, 20) + "..." : "null"));

        if (refreshToken == null || refreshToken.isBlank()) {
            System.out.println("❌ Refresh token is null or blank");
            return ResponseEntity.badRequest().body(Map.of("message", "Refresh token không được để trống"));
        }

        try {
            // 1. Verify token format và signature
            Claims claims = jwtService.extractAllClaims(refreshToken);

            System.out.println("✅ Token parsed successfully");
            System.out.println("🔍 Token type: " + claims.get("type"));
            System.out.println("🔍 Token subject: " + claims.getSubject());

            if (!"refresh".equals(claims.get("type"))) {
                System.out.println("❌ Token is not refresh type");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Token không phải refresh token"));
            }

            // 2. Find token in database
            System.out.println("🔍 Searching token in database...");
            Optional<UserToken> tokenOptional = userTokenService.findByToken(refreshToken);

            if (tokenOptional.isEmpty()) {
                System.out.println("❌ Token not found in database");

                // Debug: Kiểm tra xem có token nào của user này không
                String username = claims.getSubject();
                System.out.println("🔍 Checking all tokens for user: " + username);
                // Nếu có method findByUsername thì uncomment dòng dưới
                // List<UserToken> userTokens = userTokenService.findByUsername(username);
                // System.out.println("🔍 User has " + userTokens.size() + " tokens in DB");

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Refresh token không tồn tại trong hệ thống"));
            }

            // 3. Delete token
            UserToken tokenToDelete = tokenOptional.get();
            System.out.println("✅ Token found in database");
            System.out.println("🔍 Token ID: " + tokenToDelete.getId());
            System.out.println("🔍 Token User: " + tokenToDelete.getId());
            System.out.println("🔍 About to delete token...");

            // Sử dụng deleteById để chắc chắn
            userTokenRepository.deleteById(tokenToDelete.getId());

            // Hoặc flush để force commit ngay lập tức
            // userTokenRepository.delete(tokenToDelete);
            // userTokenRepository.flush();

            System.out.println("✅ Token deleted successfully");

            // 4. Verify deletion (Optional - for debugging)
            Optional<UserToken> verifyDeletion = userTokenService.findByToken(refreshToken);
            if (verifyDeletion.isEmpty()) {
                System.out.println("✅ Verified: Token no longer exists in database");
            } else {
                System.out.println("⚠️ Warning: Token still exists in database after deletion!");
            }

            return ResponseEntity.ok(Map.of("message", "Đăng xuất thành công, token đã bị xoá"));

        } catch (ExpiredJwtException e) {
            System.out.println("❌ Token expired: " + e.getMessage());

            // Token hết hạn nhưng vẫn cố gắng xóa khỏi DB nếu tồn tại
            try {
                Optional<UserToken> tokenOptional = userTokenService.findByToken(refreshToken);
                if (tokenOptional.isPresent()) {
                    userTokenRepository.deleteById(tokenOptional.get().getId());
                    System.out.println("✅ Expired token deleted from database");
                }
            } catch (Exception deleteEx) {
                System.out.println("❌ Error deleting expired token: " + deleteEx.getMessage());
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token đã hết hạn", "error", e.getMessage()));

        } catch (Exception e) {
            System.out.println("❌ General error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Token không hợp lệ", "error", e.getMessage()));
        }
    }

    // Optional: Thêm method để xóa tất cả token của user (logout from all devices)
//    @PostMapping("/logout-all")
//    public ResponseEntity<?> logoutAll(Authentication authentication) {
//        try {
//            String username = authentication.getName();
//            System.out.println("🔍 Logging out all devices for user: " + username);
//
//            // Assuming you have this method in your service
//            int deletedCount = userTokenService.deleteAllTokensByUsername(username);
//
//            System.out.println("✅ Deleted " + deletedCount + " tokens");
//
//            return ResponseEntity.ok(Map.of(
//                    "message", "Đăng xuất khỏi tất cả thiết bị thành công",
//                    "deletedTokens", deletedCount
//            ));
//
//        } catch (Exception e) {
//            System.out.println("❌ Error in logout-all: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("message", "Lỗi khi đăng xuất", "error", e.getMessage()));
//        }
//    }

}

