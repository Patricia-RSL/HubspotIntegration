package com.example.hubspotintegration.dto;

import java.time.Instant;

/**
 * DTO para tokens OAuth.
 */
public record OAuthTokenDTO(
        String accessToken,
        String refreshToken,
        Instant expiresAt
) {}