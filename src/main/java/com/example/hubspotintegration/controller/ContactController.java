package com.example.hubspotintegration.controller;

import com.example.hubspotintegration.service.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact")
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        this.contactService.findAll();
        return ResponseEntity.ok().build();
    }
    /*@PostMapping("/contacts")
    public ResponseEntity<String> createContact(@RequestBody ContactRequest contactRequest) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Recupere o token de acesso armazenado
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> properties = new HashMap<>();
        properties.put("email", contactRequest.getEmail());
        properties.put("firstname", contactRequest.getFirstName());
        properties.put("lastname", contactRequest.getLastName());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("properties", properties);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.hubapi.com/crm/v3/objects/contacts",
                request,
                String.class
        );

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }*/
}
