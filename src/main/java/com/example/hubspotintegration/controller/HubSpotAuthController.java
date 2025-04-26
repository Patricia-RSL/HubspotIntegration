package com.example.hubspotintegration.controller;

import com.example.hubspotintegration.service.HubSpotAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
