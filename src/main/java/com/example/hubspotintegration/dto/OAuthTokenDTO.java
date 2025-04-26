package com.example.hubspotintegration.dto;

import java.time.Instant;

public record OAuthTokenDTO(
        String accessToken,
        String refreshToken,
        Instant expiresAt
) {}