package com.example.hubspotintegration.service;

import com.example.hubspotintegration.integration.HubSpotApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class HubSpotAuthService {

    private final OAuthTokenService oAuthTokenService;
    private final HubSpotApiClient hubSpotApiClient;

    @Value("${hubspot.client.id}")
    private String clientId;

    @Value("${hubspot.client.secret}")
    private String clientSecret;

    @Value("${hubspot.redirect.uri}")
    private String redirectUri;

    @Value("${hubspot.scopes}")
    private String scopes;

    public HubSpotAuthService(OAuthTokenService oAuthTokenService, HubSpotApiClient hubSpotApiClient) {
        this.oAuthTokenService = oAuthTokenService;
        this.hubSpotApiClient = hubSpotApiClient;
    }

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

    public void handleCallback(String code) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);


        String response = hubSpotApiClient.getToken(body);
        this.oAuthTokenService.createTokenByJson(response);
    }

    public void refreshToken(String refreshToken) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);

        String response = hubSpotApiClient.refreshToken(body);
        this.oAuthTokenService.createTokenByJson(response);
    }
}
