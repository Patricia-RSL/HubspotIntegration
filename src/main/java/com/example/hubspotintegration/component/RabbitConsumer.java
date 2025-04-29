package com.example.hubspotintegration.component;

import com.example.hubspotintegration.config.RabbitConfig;
import com.example.hubspotintegration.dto.HubSpotWebhookEvent;
import com.example.hubspotintegration.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Componente responsável por consumir mensagens da fila RabbitMQ e processar eventos de criação de contatos.
 */
@Component
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Consumidor RabbitMQ", description = "Endpoints para consumo de mensagens da fila RabbitMQ")
public class RabbitConsumer {

    private final ContactService contactService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.maxAttempts:3}")
    private int maxAttempts;

    /**
     * Consome eventos de criação de contatos da fila RabbitMQ.
     * Implementa lógica de retry em caso de falha no processamento.
     *
     * @param event Evento de webhook do HubSpot contendo informações do contato
     */
    @RabbitListener(queues = "contact_create_queue")
    @Operation(
        summary = "Processar Criação de Contato",
        description = "Consome eventos de criação de contatos da fila RabbitMQ e processa o contato no HubSpot"
    )
    public void handleContactCreate(HubSpotWebhookEvent event) {
        log.info("Consumindo evento: " + event);

        try {
            contactService.processContactCreation(event);
        } catch (Exception e) {
            handleRetry(event);
        }
    }

    /**
     * Gerencia a lógica de retry para eventos que falharam no processamento.
     * Reenvia o evento para a fila principal até atingir o número máximo de tentativas.
     *
     * @param event Evento de webhook do HubSpot que falhou no processamento
     */
    @Operation(
        summary = "Gerenciar Retry",
        description = "Implementa a lógica de retry para eventos que falharam no processamento"
    )
    private void handleRetry(HubSpotWebhookEvent event) {
        if (event.getAttemptNumber() == null) {
            event.setAttemptNumber(0);
        }
        int currentAttempt = event.getAttemptNumber();
        if (currentAttempt < maxAttempts) {
            event.setAttemptNumber(currentAttempt + 1);
            log.info("Reenviando para fila principal: tentativa " + event.getAttemptNumber());
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, event.getSubscriptionType(), event);
        } else {
            log.info("Descartando webhook após " + currentAttempt + " tentativas");
        }
    }
}
