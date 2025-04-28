package com.example.hubspotintegration.dto;

import lombok.Data;
import lombok.NonNull;

/**
 * Data Transfer Object for creating a contact in HubSpot.
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
