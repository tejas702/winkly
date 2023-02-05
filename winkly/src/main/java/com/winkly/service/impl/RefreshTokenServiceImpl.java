package com.winkly.service.impl;

import com.winkly.config.JwtUtils;
import com.winkly.entity.RefreshToken;
import com.winkly.repository.UserRepository;
import com.winkly.utils.TokenRefreshException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenServiceImpl {
    @Value("${com.winkly.jwtExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public RefreshToken createRefreshToken(String email) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserEntity(userRepository.findByEmail(email).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(jwtUtils.generateRefreshTokenFromEmail(email));

        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
          throw new TokenRefreshException(
              token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }
}
