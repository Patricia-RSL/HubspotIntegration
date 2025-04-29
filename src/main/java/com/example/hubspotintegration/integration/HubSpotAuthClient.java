package com.example.hubspotintegration.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cliente Feign para interação com a API de autenticação do HubSpot.
 * Este cliente é responsável por realizar as requisições de autenticação
 * OAuth2 com o HubSpot, incluindo obtenção de tokens de acesso e
 * atualização de tokens expirados.
 */
@FeignClient(name = "hubspotAuth",
        url = "https://api.hubapi.com")
public interface HubSpotAuthClient {

    /**
     * Obtém um token de acesso do HubSpot utilizando o código de autorização.
     *
     * @param form Parâmetros do formulário contendo o código de autorização
     * @return String contendo o token de acesso em formato JSON
     */
    @PostMapping("/oauth/v1/token")
    String getToken(@RequestBody MultiValueMap<String, String> form);

    /**
     * Atualiza um token de acesso expirado utilizando o refresh token.
     *
     * @param form Parâmetros do formulário contendo o refresh token
     * @return String contendo o novo token de acesso em formato JSON
     */
    @PostMapping("/oauth/v1/token")
    String refreshToken(@RequestBody MultiValueMap<String, String> form);
}
