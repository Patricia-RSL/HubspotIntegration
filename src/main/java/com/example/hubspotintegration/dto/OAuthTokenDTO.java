package com.example.hubspotintegration.dto;

import java.time.Instant;

/**
 * Data Transfer Object for OAuth tokens.
 */
public record OAuthTokenDTO(
        String accessToken,
        String refreshToken,
        Instant expiresAt
) {}