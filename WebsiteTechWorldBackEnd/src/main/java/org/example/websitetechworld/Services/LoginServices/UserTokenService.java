package org.example.websitetechworld.Services.LoginServices;

import org.example.websitetechworld.Entity.UserToken;

import java.util.Optional;

public interface UserTokenService {
    Optional<UserToken> findByToken(String token);
    CustomUserDetails loadUserByToken(UserToken token);
    void saveTokens(CustomUserDetails user, String accessToken, String refreshToken);
}
