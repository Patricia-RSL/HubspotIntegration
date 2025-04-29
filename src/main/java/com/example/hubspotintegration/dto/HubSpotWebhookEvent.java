package com.example.hubspotintegration.dto;

import lombok.Data;

/**
 * DTO para eventos de webhook do HubSpot.
 */
@Data
public class HubSpotWebhookEvent {
    private Long appId;
    private Long eventId;
    private Long subscriptionId;
    private Long portalId;
    private Long occurredAt;
    private String subscriptionType;
    private Integer attemptNumber;
    private Long objectId;
    private String changeSource;
    private String changeFlag;
}
