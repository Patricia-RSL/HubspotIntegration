package com.example.hubspotintegration.service;

import com.example.hubspotintegration.dto.HubSpotWebhookEvent;
import com.example.hubspotintegration.integration.HubSpotContactClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Serviço responsável por gerenciar todas as operações relacionadas a contatos no HubSpot.
 * Este serviço fornece funcionalidades para criar, consultar e processar eventos
 * de contatos através da API do HubSpot.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ContactService {

    private final HubSpotContactClient hubSpotContactClient;

    /**
     * Consulta todos os contatos cadastrados no HubSpot.
     * Este método faz uma chamada à API do HubSpot para obter a lista completa
     * de contatos e retorna os dados em formato JSON.
     *
     * @return JsonNode contendo a lista de contatos
     * @throws JsonProcessingException Se houver erro ao processar a resposta JSON
     */
    public JsonNode findAll() throws JsonProcessingException {
        String contacts = hubSpotContactClient.fetchContacts();
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readTree(contacts);
    }

    /**
     * Cria um novo contato no HubSpot com os dados fornecidos.
     * Este método envia uma requisição para a API do HubSpot com as informações
     * básicas do contato (email, nome e sobrenome).
     *
     * @param email Email do contato
     * @param firstName Nome do contato
     * @param lastName Sobrenome do contato
     * @return JsonNode contendo a resposta da API com os dados do contato criado
     * @throws JsonProcessingException Se houver erro ao processar a resposta JSON
     */
    public JsonNode createContact(String email, String firstName, String lastName) throws JsonProcessingException {
         String response = hubSpotContactClient.createContact(Map.of(
                "properties", Map.of(
                        "email", email,
                        "firstname", firstName,
                        "lastname", lastName
                )
        ));

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readTree(response);
    }

    /**
     * Processa eventos de criação de contatos recebidos via webhook.
     * Este método é chamado quando um evento de criação de contato é recebido
     * do HubSpot e realiza o processamento necessário.
     *
     * @param event Evento de webhook contendo informações sobre o contato criado
     */
    public void processContactCreation(HubSpotWebhookEvent event) {
        if (event == null) {
            log.warn("Webhook inválido : {}", event);
            return;
        }
        log.info("Processando evento de criação de contato: {}", event);
    }
}
