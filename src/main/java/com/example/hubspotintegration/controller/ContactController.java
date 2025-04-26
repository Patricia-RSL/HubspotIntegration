package com.example.hubspotintegration.controller;

import com.example.hubspotintegration.dto.ContactRequestDTO;
import com.example.hubspotintegration.service.ContactService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<JsonNode> getAll() {
        try {
            JsonNode response = this.contactService.findAll();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleError(e);
        }
    }

    @PostMapping
    public ResponseEntity<JsonNode> createContact(@RequestBody ContactRequestDTO contactRequestDTO) {
        try {
            JsonNode response = this.contactService.createContact(
                    contactRequestDTO.getEmail(),
                    contactRequestDTO.getFirstName(),
                    contactRequestDTO.getLastName()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleError(e);
        }
    }

    private ResponseEntity<JsonNode> handleError(Exception e) {
        JsonNode errorResponse = convertToErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private JsonNode convertToErrorResponse(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("status", "error");
        errorNode.put("message", message);
        return errorNode;
    }
}
