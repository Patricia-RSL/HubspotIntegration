package com.example.hubspotintegration.controller;

import com.example.hubspotintegration.config.RabbitConfig;
import com.example.hubspotintegration.dto.HubSpotWebhookEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

/**
 * Controller para processamento de webhooks do HubSpot.
 */
@RestController
@RequestMapping("/webhook")
@AllArgsConstructor
@Slf4j
@Tag(name = "Webhooks", description = "Endpoints para processamento de webhooks do HubSpot")
public class WebhookController {

    private RabbitTemplate rabbitTemplate;

    @Operation(summary = "Processar webhook", description = "Recebe e processa eventos de webhook do HubSpot, enviando-os para a fila RabbitMQ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Eventos processados com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados do webhook inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Lista de eventos de webhook do HubSpot",
                required = true,
                content = @Content(schema = @Schema(implementation = HubSpotWebhookEvent.class))
            )
            @RequestBody List<HubSpotWebhookEvent> events) {
        for (HubSpotWebhookEvent event : events) {
            log.info("Evento recebido: " + event);
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, event.getSubscriptionType(), event);
            log.info("Evento enviado para RabbitMQ: " + event);
        }
        return ResponseEntity.ok("Eventos processados com sucesso");
    }
}
