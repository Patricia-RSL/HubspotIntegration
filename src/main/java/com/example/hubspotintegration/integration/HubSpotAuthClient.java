package com.example.hubspotintegration.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Feign client for interacting with HubSpot API.
 * This client is used to fetch contacts, create contacts, and handle OAuth token requests.
 */
@FeignClient(name = "hubspotAuth",
        url = "https://api.hubapi.com")
public interface HubSpotAuthClient {

    @PostMapping("/oauth/v1/token")
    String getToken(@RequestBody MultiValueMap<String, String> form);

    @PostMapping("/oauth/v1/token")
    String refreshToken(@RequestBody MultiValueMap<String, String> form);
}
