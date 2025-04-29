package com.example.hubspotintegration.controller;

import com.example.hubspotintegration.service.AuthService;
import com.example.hubspotintegration.service.OAuthTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador responsável por gerenciar todo o fluxo de autenticação OAuth2 com o HubSpot.
 * Este controlador lida com a geração de URLs de autorização e o processamento de callbacks
 * para obtenção de tokens de acesso.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação HubSpot", description = "Endpoints para gerenciamento do fluxo de autenticação OAuth2 com o HubSpot")
public class HubSpotAuthController {

    private final AuthService authService;
    private final OAuthTokenService oAuthTokenService;

    public HubSpotAuthController(AuthService authService, OAuthTokenService oAuthTokenService) {
        this.authService = authService;
        this.oAuthTokenService = oAuthTokenService;
    }

    @Operation(summary = "Obter URL de autorização", description = "Retorna a URL para iniciar o processo de autorização OAuth com o HubSpot")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "URL de autorização gerada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/url")
    public ResponseEntity<String> getAuthorizationUrl() {
        String authUrl = this.authService.generateUrl();
        return ResponseEntity.ok(authUrl);
    }

    @Operation(summary = "Processar callback de autorização", description = "Processa o código de autorização retornado pelo HubSpot e gera o token de acesso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autorização concluída com sucesso"),
        @ApiResponse(responseCode = "400", description = "Código de autorização inválido"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(
            @Parameter(description = "Código de autorização retornado pelo HubSpot", required = true)
            @RequestParam("code") String code) throws JsonProcessingException {
        String jsonToken = this.authService.getTokenFromCode(code);
        if (jsonToken == null) {
            throw new IllegalArgumentException("Não foi possível obter o token de acesso.");
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
                <h1>Autorização Concluída</h1>
                <p>Agora você pode usar a API do HubSpot.</p>
            </div>
        </body>
        </html>
        """);
    }
}
