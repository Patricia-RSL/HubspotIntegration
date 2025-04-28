package com.example.hubspotintegration.component;

import com.example.hubspotintegration.dto.OAuthTokenDTO;

import com.example.hubspotintegration.service.OAuthTokenService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Interceptor for adding OAuth2 Bearer token to the request header.
 */
@Component("hubSpotFeignInterceptor")
@RequiredArgsConstructor
public class HubSpotFeignInterceptor implements RequestInterceptor {
    private final OAuthTokenService oAuthTokenService;


    /**
     * Applies the OAuth2 Bearer token to the request header.
     *
     * @param requestTemplate the request template
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (!requestTemplate.url().contains("/oauth")) {
            OAuthTokenDTO accessToken = oAuthTokenService.getLatestToken();
            if (accessToken == null || accessToken.accessToken() == null || accessToken.expiresAt().isBefore(Instant.now())) {
                throw new IllegalStateException("No valid access token available to add to the request header");
            }
            requestTemplate.header("Authorization", "Bearer " + accessToken.accessToken());
        }
    }
}