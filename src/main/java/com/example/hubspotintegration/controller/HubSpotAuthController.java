package com.example.hubspotintegration.controller;

import com.example.hubspotintegration.service.HubSpotAuthService;
import com.example.hubspotintegration.service.OAuthTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/auth")
public class HubSpotAuthController {

    private final HubSpotAuthService hubSpotAuthService;

    public HubSpotAuthController(HubSpotAuthService hubSpotAuthService) {
        this.hubSpotAuthService = hubSpotAuthService;
    }

    @GetMapping("/url")
    public ResponseEntity<String> getAuthorizationUrl() {
        String authUrl = this.hubSpotAuthService.generateUrl();
        return ResponseEntity.ok(authUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("code") String code) throws JsonProcessingException {
        return ResponseEntity.ok(this.hubSpotAuthService.handleCallback(code));
    }
}
