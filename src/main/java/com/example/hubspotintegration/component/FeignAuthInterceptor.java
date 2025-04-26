package com.example.hubspotintegration.component;

import com.example.hubspotintegration.dto.OAuthTokenDTO;
import com.example.hubspotintegration.service.OAuthTokenService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FeignAuthInterceptor implements RequestInterceptor {
    private final OAuthTokenService oAuthTokenService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        OAuthTokenDTO accessToken = oAuthTokenService.getLatestToken();
        if (accessToken == null) {
            throw new IllegalStateException("No valid access token available to add to the request header");
        }
        requestTemplate.header("Authorization", "Bearer " + accessToken.accessToken());
    }
}