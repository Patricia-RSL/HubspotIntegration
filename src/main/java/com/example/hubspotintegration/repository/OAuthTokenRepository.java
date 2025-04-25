package com.example.hubspotintegration.repository;

import com.example.hubspotintegration.entity.OAuthToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthTokenRepository extends JpaRepository<OAuthToken, Long> {
}