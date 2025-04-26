package com.example.hubspotintegration.component;

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
        String accessToken = oAuthTokenService.getLatestToken().accessToken();
        requestTemplate.header("Authorization", "Bearer " + accessToken);
    }
}