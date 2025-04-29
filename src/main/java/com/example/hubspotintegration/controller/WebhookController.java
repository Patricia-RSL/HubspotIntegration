package com.example.hubspotintegration.controller;

import com.example.hubspotintegration.config.RabbitConfig;
import com.example.hubspotintegration.dto.HubSpotWebhookEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

@RestController
@RequestMapping("/webhook")
@AllArgsConstructor
@Slf4j
public class WebhookController {

    private RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody List<HubSpotWebhookEvent> events) {
        for (HubSpotWebhookEvent event : events) {
            log.info("Evento recebido: " + event);
            try {
                rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, event.getSubscriptionType(), event);
                log.info("Evento enviado para RabbitMQ: " + event);
                return ResponseEntity.ok("Evento recebido e enviado para RabbitMQ");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Erro ao receber webhook: " + e.getMessage());
            }
        }
        return ResponseEntity.ok("Eventos processados com sucesso");
    }
}
