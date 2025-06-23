package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;

public interface TokenService {
    String generateAccessToken(CustomUserDetails user);
    String generateRefreshToken(CustomUserDetails user);
}

