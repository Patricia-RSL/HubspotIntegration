package com.example.hubspotintegration.controller;

import com.example.hubspotintegration.dto.ContactRequestDTO;
import com.example.hubspotintegration.service.ContactService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * Controller for managing HubSpot contacts.
 */
@RestController
@RequestMapping("/contact")
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<JsonNode> getAll() {
        try {
            return ResponseEntity.ok(contactService.findAll());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch contacts", ex);
        }
    }

    @PostMapping
    public ResponseEntity<JsonNode> createContact(@RequestBody @Valid ContactRequestDTO contactRequestDTO) throws JsonProcessingException {
        JsonNode response = this.contactService.createContact(
                contactRequestDTO.getEmail(),
                contactRequestDTO.getFirstName(),
                contactRequestDTO.getLastName()
        );
        return ResponseEntity.ok(response);
    }


}
