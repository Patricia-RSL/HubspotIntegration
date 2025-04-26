package com.example.hubspotintegration.service;

import com.example.hubspotintegration.integration.HubSpotApiClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContactService {

    private final HubSpotApiClient hubSpotApiClient;

    public void findAll() {
        String contacts = hubSpotApiClient.fetchContacts();
        System.out.println("Contacts: " + contacts);
    }
}
