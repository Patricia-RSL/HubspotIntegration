package com.example.hubspotintegration.integration;

import com.example.hubspotintegration.component.HubSpotFeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "hubspotContact",
        url = "https://api.hubapi.com",
        configuration = HubSpotFeignInterceptor.class)
public interface HubSpotContactClient {

    @GetMapping("/crm/v3/objects/contacts")
    String fetchContacts();

    @PostMapping("/crm/v3/objects/contacts")
    String createContact(@RequestBody Map<String, Object> properties);
}

