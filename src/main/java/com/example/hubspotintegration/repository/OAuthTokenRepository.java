package com.example.hubspotintegration.repository;

import com.example.hubspotintegration.entity.OAuthToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório para gerenciar tokens OAuth.
 */
public interface OAuthTokenRepository extends JpaRepository<OAuthToken, Long> {
    Optional<OAuthToken> findTopByOrderByCreatedAtDesc();

}