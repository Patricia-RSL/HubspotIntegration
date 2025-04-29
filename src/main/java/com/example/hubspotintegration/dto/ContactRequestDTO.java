package com.example.hubspotintegration.dto;

import lombok.Data;
import lombok.NonNull;

/**
 * DTO para solicitação de criação de contato no HubSpot.
 */
@Data
public class ContactRequestDTO {

    @NonNull
    private String email;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;
}
