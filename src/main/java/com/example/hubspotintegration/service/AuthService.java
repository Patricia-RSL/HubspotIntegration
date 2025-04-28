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
 * Service for handling HubSpot OAuth authentication.
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

    public String  refreshToken(String refreshToken) {

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
