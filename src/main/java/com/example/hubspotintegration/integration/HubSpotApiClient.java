package com.example.hubspotintegration.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "hubspot", url = "https://api.hubapi.com")
public interface HubSpotApiClient {
 //crm.objects.contacts.read
    @GetMapping("/crm/v3/objects/contacts")
    String fetchContacts();
}
