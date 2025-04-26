package com.example.hubspotintegration.service;

import com.example.hubspotintegration.dto.OAuthTokenDTO;
import com.example.hubspotintegration.entity.OAuthToken;
import com.example.hubspotintegration.repository.OAuthTokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        OAuthToken token = tokenRepository
                .findTopByOrderByCreatedAtDesc()
                .orElseThrow(() -> new IllegalStateException("No valid access token available to add to the request header"));

        String accessToken = encryptor.decrypt(token.getAccessToken());
        String refreshToken = encryptor.decrypt(token.getRefreshToken());

        return new OAuthTokenDTO(accessToken, refreshToken, token.getExpiresAt());
    }

    public void createTokenByJson(String jsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonStr);

        String accessToken = jsonNode.get("access_token").asText();
        String refreshToken = jsonNode.get("refresh_token").asText();
        Long expiresIn = jsonNode.get("expires_in").asLong();

        this.saveToken(accessToken, refreshToken, expiresIn);
    }
}
