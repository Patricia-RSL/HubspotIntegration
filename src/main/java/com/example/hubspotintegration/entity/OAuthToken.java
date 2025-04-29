package com.example.hubspotintegration.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

/**
 * Entidade que representa tokens OAuth.
 */
@Entity
@Data
@Table(name = "oauth_tokens")
public class OAuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2048)
    private String accessToken;

    @Column(length = 2048)
    private String refreshToken;

    private Instant expiresAt;

    private Instant createdAt;

}
