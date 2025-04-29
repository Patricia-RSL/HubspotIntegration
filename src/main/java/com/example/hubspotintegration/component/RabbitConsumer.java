package com.example.hubspotintegration.component;

import com.example.hubspotintegration.config.RabbitConfig;
import com.example.hubspotintegration.dto.HubSpotWebhookEvent;
import com.example.hubspotintegration.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitConsumer {

    private final ContactService contactService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.maxAttempts:3}")
    private int maxAttempts;

    @RabbitListener(queues = "contact_create_queue")
    public void handleContactCreate(HubSpotWebhookEvent event) {
        log.info("Consumindo evento: " + event);

        try {
            contactService.processContactCreation(event);
        } catch (Exception e) {
            handleRetry(event);
        }
    }

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
