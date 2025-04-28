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
 * Service for managing HubSpot contacts.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ContactService {

    private final HubSpotContactClient hubSpotContactClient;

    public JsonNode findAll() throws JsonProcessingException {
        String contacts = hubSpotContactClient.fetchContacts();
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readTree(contacts);
    }

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

    public void processContactCreation(HubSpotWebhookEvent event) {
        if (event == null) {
            log.warn("Webhook inválido : {}", event);
            return;
        }
        log.info("Processando evento de criação de contato: {}", event);
    }
}
