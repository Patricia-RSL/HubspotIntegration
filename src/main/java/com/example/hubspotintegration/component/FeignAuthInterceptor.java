package com.example.hubspotintegration.component;

import com.example.hubspotintegration.dto.OAuthTokenDTO;
import com.example.hubspotintegration.service.HubSpotAuthService;
import com.example.hubspotintegration.service.OAuthTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@AllArgsConstructor
public class FeignAuthInterceptor implements RequestInterceptor {
    private final OAuthTokenService oAuthTokenService;
    private final HubSpotAuthService hubSpotAuthService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (!requestTemplate.url().startsWith("/oauth")) {
            OAuthTokenDTO accessToken = oAuthTokenService.getLatestToken();
            if (accessToken == null) {
                throw new IllegalStateException("No valid access token available to add to the request header");
            }
            if (accessToken.expiresAt().isBefore(Instant.now())) {
                try {
                    hubSpotAuthService.refreshToken(accessToken.refreshToken());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            requestTemplate.header("Authorization", "Bearer " + accessToken.accessToken());
        }
    }
}