package com.example.hubspotintegration.service;

import com.example.hubspotintegration.integration.HubSpotAuthClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Serviço responsável por gerenciar a autenticação OAuth2 com o HubSpot.
 * Este serviço lida com a geração de URLs de autorização, obtenção de tokens
 * de acesso e refresh tokens para manter a autenticação ativa.
 */
@Service
public class AuthService {

    private final HubSpotAuthClient hubSpotAuthClient;

    @Value("${hubspot.client.id}")
    private String clientId;

    @Value("${hubspot.client.secret}")
    private String clientSecret;

    @Value("${hubspot.redirect.uri}")
    private String redirectUri;

    @Value("${hubspot.scopes}")
    private String scopes;

    public AuthService(HubSpotAuthClient hubSpotAuthClient) {
        this.hubSpotAuthClient = hubSpotAuthClient;
    }

    /**
     * Gera a URL de autorização OAuth2 para iniciar o processo de autenticação com o HubSpot.
     * Esta URL contém os parâmetros necessários para a autorização, incluindo client_id,
     * escopos solicitados e URI de redirecionamento.
     *
     * @return String contendo a URL completa para autorização
     */
    public String generateUrl() {
        return UriComponentsBuilder
                .fromHttpUrl("https://app.hubspot.com/oauth/authorize")
                .queryParam("client_id",    clientId)
                .queryParam("scope",        scopes)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("state",        "STATE")
                .encode()
                .toUriString();
    }

    /**
     * Obtém o token de acesso do HubSpot utilizando o código de autorização.
     * Este método é chamado após o usuário autorizar o acesso e o HubSpot
     * redirecionar para a URI de callback com o código de autorização.
     *
     * @param code Código de autorização retornado pelo HubSpot
     * @return String contendo o token de acesso em formato JSON
     */
    public String getTokenFromCode(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        return hubSpotAuthClient.getToken(body);
    }

    /**
     * Atualiza o token de acesso utilizando o refresh token.
     * Este método é utilizado quando o token de acesso expira e
     * é necessário obter um novo token sem requerer nova autorização do usuário.
     *
     * @param refreshToken Token de atualização obtido durante a autorização inicial
     * @return String contendo o novo token de acesso em formato JSON
     */
    public String refreshToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);

        return hubSpotAuthClient.refreshToken(body);
    }
}
