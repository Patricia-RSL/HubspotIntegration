package com.example.hubspotintegration.controller;

import com.example.hubspotintegration.dto.HubSpotWebhookEvent;
import com.example.hubspotintegration.service.ContactService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/webhook")
@AllArgsConstructor
@Slf4j
public class HubSpotWebhookController {

    private final ContactService contactService;


    @PostMapping("/contact-creation")
    public ResponseEntity<String> handleContactCreation(@RequestBody List<HubSpotWebhookEvent> webhookEvent) {
        log.info("Recebido evento do webhook: {}", webhookEvent);

        try {
            if (!"contact.creation".equals(webhookEvent.get(0).getSubscriptionType())) {
                return ResponseEntity.badRequest().body("Evento inválido ou não suportado.");
            }
            contactService.processContactCreation(webhookEvent.get(0));

            return ResponseEntity.ok("Evento processado com sucesso.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao processar evento: " + e.getMessage());
        }
    }
}
