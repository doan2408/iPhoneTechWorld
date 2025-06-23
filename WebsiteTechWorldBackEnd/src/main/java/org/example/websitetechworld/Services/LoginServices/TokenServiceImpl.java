package org.example.websitetechworld.Services.LoginServices;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Repository.JointAccount;
import org.example.websitetechworld.Repository.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.secret}")
    private String secretKey;// hoặc dùng @Value cấu hình ngoài

    private static final long ACCESS_TOKEN_EXP_MS = 2 * 60 * 60 * 1000;
    private static final long REFRESH_TOKEN_EXP_MS = 7 * 24 * 60 * 60 * 1000;


    @PostConstruct
    public void init() {
        System.out.println("TokenService Secret: " + secretKey);
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("JWT_SECRET không được cấu hình!");
        }
    }
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAccessToken(CustomUserDetails user) {
        JointAccount account = user.getAccount(); // chính là đối tượng JointAccount

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("id", account.getId())
                .claim("fullName", account.getFullName())
                .claim("role", account.getRole())
                .claim("email", account.getEmail())
                .claim("sdt", account.getSdt())
                .claim("trangThai", account.getTrangThai())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(CustomUserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXP_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}

