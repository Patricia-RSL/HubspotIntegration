package com.example.hubspotintegration.service;

import com.example.hubspotintegration.dto.OAuthTokenDTO;
import com.example.hubspotintegration.entity.OAuthToken;
import com.example.hubspotintegration.repository.OAuthTokenRepository;
import lombok.AllArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class OAuthTokenService {

    private OAuthTokenRepository tokenRepository;
    private final StringEncryptor encryptor;

    public void saveToken(String accessToken, String refreshToken, Long expiresIn) {
        OAuthToken token = new OAuthToken();
        token.setAccessToken(encryptor.encrypt(accessToken));
        token.setRefreshToken(encryptor.encrypt(refreshToken));
        token.setExpiresAt(Instant.now().plusSeconds(expiresIn));
        token.setCreatedAt(Instant.now());

        tokenRepository.save(token);
    }

    public OAuthTokenDTO getLatestToken() {
        OAuthToken t = tokenRepository
                .findTopByOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("Nenhum token encontrado"));

        String at = encryptor.decrypt(t.getAccessToken());
        String rt = encryptor.decrypt(t.getRefreshToken());
        return new OAuthTokenDTO(at, rt, t.getExpiresAt());
    }

}
