package org.example.websitetechworld.Services.LoginServices;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.example.websitetechworld.Entity.KhachHang;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey key;

    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("JWT_SECRET không được cấu hình!");
        }
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        System.out.println("🔐 Secret key init thành công.");
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return buildToken(claims, username, 1000 * 60 * 60 * 2); // 2 giờ
    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return buildToken(claims, username, 1000 * 60 * 60 * 24 * 7); // 7 ngày
    }

    private String buildToken(Map<String, Object> claims, String subject, long expirationMillis) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(KhachHang khachHang) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", khachHang.getId());
        claims.put("email", khachHang.getEmail());
        claims.put("fullName", khachHang.getTenKhachHang());
        claims.put("role", khachHang.getRole()); // Nếu có role
        claims.put("trangThai", khachHang.getTrangThai());

        return buildToken(claims, khachHang.getTaiKhoan(), 1000 * 60 * 60 * 2); // 2 giờ
    }

    public String generateRefreshToken(KhachHang khachHang) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return buildToken(claims, khachHang.getTaiKhoan(), 1000 * 60 * 60 * 24 * 7); // 7 ngày
    }


}
