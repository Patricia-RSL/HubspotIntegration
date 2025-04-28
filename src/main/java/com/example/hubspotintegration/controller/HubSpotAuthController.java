package com.example.hubspotintegration.controller;

import com.example.hubspotintegration.service.AuthService;
import com.example.hubspotintegration.service.OAuthTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling HubSpot OAuth authentication.
 */
@RestController
@RequestMapping("/auth")
public class HubSpotAuthController {

    private final AuthService authService;
    private final OAuthTokenService oAuthTokenService;

    public HubSpotAuthController(AuthService authService, OAuthTokenService oAuthTokenService) {
        this.authService = authService;
        this.oAuthTokenService = oAuthTokenService;
    }

    @GetMapping("/url")
    public ResponseEntity<String> getAuthorizationUrl() {
        String authUrl = this.authService.generateUrl();
        return ResponseEntity.ok(authUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("code") String code) throws JsonProcessingException {
        String jsonToken = this.authService.getTokenFromCode(code);
        if (jsonToken == null) {
            throw new IllegalArgumentException("Failed to obtain token");
        }
        this.oAuthTokenService.createTokenByJson(jsonToken);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body("""
        <html>
        <head>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    background-color: #f4f4f9;
                    margin: 0;
                    padding: 0;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    height: 100vh;
                }
                .container {
                    text-align: center;
                    background: white;
                    padding: 20px 30px;
                    border-radius: 8px;
                    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                }
                h1 {
                    color: #4CAF50;
                    margin-bottom: 10px;
                }
                p {
                    color: #333;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <h1>Authorization Successful</h1>
                <p>You can now use the HubSpot API.</p>
            </div>
        </body>
        </html>
        """);
    }
}
