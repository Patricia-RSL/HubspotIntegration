package com.example.hubspotintegration.service;

import com.example.hubspotintegration.entity.OAuthToken;
import com.example.hubspotintegration.repository.OAuthTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class OAuthTokenService {

    private OAuthTokenRepository tokenRepository;

    public void saveToken(String accessToken, String refreshToken, Instant expiresAt) {
        OAuthToken token = new OAuthToken();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setExpiresAt(expiresAt);
        token.setCreatedAt(Instant.now());

        tokenRepository.save(token);
    }

}
