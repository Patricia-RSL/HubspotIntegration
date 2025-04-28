package com.example.hubspotintegration.dto;

import lombok.Data;

/**
 * Data Transfer Object for creating a contact in HubSpot.
 */
@Data
public class ContactRequestDTO {

    private String email;
    private String firstName;
    private String lastName;
}
