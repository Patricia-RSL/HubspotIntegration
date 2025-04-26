package com.example.hubspotintegration.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "hubspot", url = "https://api.hubapi.com")
public interface HubSpotApiClient {

    @GetMapping("/crm/v3/objects/contacts")
    String fetchContacts();


    @PostMapping("/crm/v3/objects/contacts")
    String createContact(@RequestBody Map<String, Object> properties);
}
