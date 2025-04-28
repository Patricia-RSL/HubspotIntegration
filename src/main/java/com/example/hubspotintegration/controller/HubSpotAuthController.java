package com.example.hubspotintegration.controller;

import com.example.hubspotintegration.service.HubSpotAuthService;
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
        this.hubSpotAuthService.handleCallback(code);
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
