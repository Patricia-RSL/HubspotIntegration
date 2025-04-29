package com.example.hubspotintegration.component;

import com.example.hubspotintegration.dto.OAuthTokenDTO;
import com.example.hubspotintegration.service.OAuthTokenService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Interceptor para adicionar o token de acesso OAuth2 no cabeçalho das requisições.
 */
@Component("hubSpotFeignInterceptor")
@RequiredArgsConstructor
public class HubSpotFeignInterceptor implements RequestInterceptor {
    private final OAuthTokenService oAuthTokenService;

    /**
     * Adiciona o token de acesso OAuth2 no cabeçalho da requisição.
     * Este método é chamado automaticamente pelo Feign antes de cada requisição.
     * Ele verifica se o token está válido e o adiciona no formato Bearer.
     * 
     * @param requestTemplate Template da requisição que será enviada
     * @throws IllegalStateException Se não houver token de acesso válido
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (!requestTemplate.url().contains("/oauth")) {
            OAuthTokenDTO accessToken = oAuthTokenService.getLatestToken();
            if (accessToken == null || accessToken.accessToken() == null || accessToken.expiresAt().isBefore(Instant.now())) {
                throw new IllegalStateException("Não é possível se conectar ao HubSPot. Nenhum token de acesso encontrado ou o token está expirado.");
            }
            requestTemplate.header("Authorization", "Bearer " + accessToken.accessToken());
        }
    }
}