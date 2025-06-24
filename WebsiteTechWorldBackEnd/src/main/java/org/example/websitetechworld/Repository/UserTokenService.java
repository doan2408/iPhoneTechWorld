package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.UserToken;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;

import java.util.Optional;

public interface UserTokenService {
    Optional<UserToken> findByToken(String token);
    CustomUserDetails loadUserByToken(UserToken token);
    void saveTokens(CustomUserDetails user, String accessToken, String refreshToken);
}
