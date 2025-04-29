package com.example.hubspotintegration.service;

import com.example.hubspotintegration.dto.OAuthTokenDTO;
import com.example.hubspotintegration.entity.OAuthToken;
import com.example.hubspotintegration.repository.OAuthTokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Serviço responsável por gerenciar tokens de autenticação OAuth2 do HubSpot.
 * Este serviço lida com o armazenamento seguro, recuperação e atualização
 * de tokens de acesso e refresh tokens, garantindo a continuidade da autenticação.
 */
@Service
@AllArgsConstructor
public class OAuthTokenService {

    private OAuthTokenRepository tokenRepository;
    private AuthService authService;
    private final StringEncryptor encryptor;

    /**
     * Salva um novo token de acesso no banco de dados.
     * Os tokens são criptografados antes de serem armazenados por questões de segurança.
     *
     * @param accessToken Token de acesso fornecido pelo HubSpot
     * @param refreshToken Token de atualização fornecido pelo HubSpot
     * @param expiresIn Tempo em segundos até a expiração do token
     */
    public void saveToken(String accessToken, String refreshToken, Long expiresIn) {
        OAuthToken token = new OAuthToken();
        token.setAccessToken(encryptor.encrypt(accessToken));
        token.setRefreshToken(encryptor.encrypt(refreshToken));
        token.setExpiresAt(Instant.now().plusSeconds(expiresIn));
        token.setCreatedAt(Instant.now());

        tokenRepository.save(token);
    }

    /**
     * Cria e salva um novo token a partir de uma resposta JSON do HubSpot.
     * Este método é utilizado após a autorização inicial ou atualização do token.
     *
     * @param jsonStr Resposta JSON do HubSpot contendo os tokens
     * @throws JsonProcessingException Se houver erro ao processar o JSON
     */
    public void createTokenByJson(String jsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonStr);

        String accessToken = jsonNode.get("access_token").asText();
        String refreshToken = jsonNode.get("refresh_token").asText();
        Long expiresIn = jsonNode.get("expires_in").asLong();

        this.saveToken(accessToken, refreshToken, expiresIn);
    }

    /**
     * Obtém o token mais recente do banco de dados.
     * Se o token estiver expirado, automaticamente solicita uma atualização.
     *
     * @return DTO contendo os tokens de acesso e atualização
     * @throws IllegalStateException Se nenhum token for encontrado
     */
    public OAuthTokenDTO getLatestToken() {
        OAuthToken token = tokenRepository
                .findTopByOrderByCreatedAtDesc()
                .orElseThrow(() -> new IllegalStateException("Nenhum token de acesso ao HubSpot encontrado."));

        String accessToken = encryptor.decrypt(token.getAccessToken());
        String refreshToken = encryptor.decrypt(token.getRefreshToken());

        OAuthTokenDTO tokenDTO = new OAuthTokenDTO(accessToken, refreshToken, token.getExpiresAt());

        return validateAndRefreshToken(tokenDTO);
    }

    /**
     * Valida se o token está expirado e, se necessário, solicita uma atualização.
     * Este método é chamado internamente para garantir que sempre temos um token válido.
     *
     * @param tokenDTO DTO contendo os tokens a serem validados
     * @return DTO com o token atualizado ou o mesmo token se ainda for válido
     * @throws IllegalStateException Se houver falha ao atualizar o token
     */
    private OAuthTokenDTO validateAndRefreshToken(OAuthTokenDTO tokenDTO) {
        if (tokenDTO.expiresAt().isBefore(Instant.now())) {
            try {
                String newToken = authService.refreshToken(tokenDTO.refreshToken());
                this.createTokenByJson(newToken);
                return getLatestToken();
            } catch (Exception e) {
                throw new IllegalStateException("Falha ao atualizar token de acesso ao HubSpot", e);
            }
        }
        return tokenDTO;
    }
}
