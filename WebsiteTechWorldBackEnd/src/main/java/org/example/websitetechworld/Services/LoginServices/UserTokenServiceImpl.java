package org.example.websitetechworld.Services.LoginServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.NhanVien;
import org.example.websitetechworld.Entity.UserToken;
import org.example.websitetechworld.Repository.JointAccount;
import org.example.websitetechworld.Repository.UserTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserTokenServiceImpl implements UserTokenService {

    private final UserTokenRepository tokenRepository;

    @Override
    public void saveTokens(CustomUserDetails user, String accessToken, String refreshToken) {
        // Xoá token cũ nếu cần
        tokenRepository.deleteByToken(refreshToken);

        UserToken token = new UserToken();
        token.setAccessToken(accessToken);
        token.setToken(refreshToken);
        token.setTokenType("refresh");
        token.setCreatedAt(Instant.now());
        token.setExpiresAt((Instant.now().plusSeconds(7 * 24 * 60 * 60))); // 7 ngày

        if ("ROLE_STAFF".equals(user.getAccount().getRole()) || "ROLE_ADMIN".equals(user.getAccount().getRole())) {
            NhanVien nv = new NhanVien();
            nv.setId(user.getId());
            token.setIdNhanVien(nv);
        } else {
            KhachHang kh = new KhachHang();
            kh.setId(user.getId());
            token.setIdKhachHang(kh);
        }

        tokenRepository.save(token);
    }

    @Override
    public Optional<UserToken> findByToken(String token) {
        return tokenRepository.findByTokenAndTokenType(token, "refresh");
    }

    @Override
    public CustomUserDetails loadUserByToken(UserToken token) {
        JointAccount account;

        if (token.getIdNhanVien() != null) {
            account = token.getIdNhanVien(); // đã được fetch bởi JPA do ManyToOne
        } else if (token.getIdKhachHang() != null) {
            account = token.getIdKhachHang();
        } else {
            throw new RuntimeException("Token không chứa thông tin người dùng hợp lệ.");
        }

        return new CustomUserDetails(account);
    }

}