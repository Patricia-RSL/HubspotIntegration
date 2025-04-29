package com.example.hubspotintegration.controller;

import com.example.hubspotintegration.dto.ContactRequestDTO;
import com.example.hubspotintegration.service.ContactService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controller para gerenciamento de contatos do HubSpot.
 */
@RestController
@RequestMapping("/contact")
@AllArgsConstructor
@Tag(name = "Contatos", description = "Endpoints para gerenciamento de contatos no HubSpot")
public class ContactController {

    private final ContactService contactService;

    @Operation(summary = "Listar todos os contatos", description = "Retorna uma lista de todos os contatos cadastrados no HubSpot")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de contatos retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonNode.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<JsonNode> getAll() throws JsonProcessingException {
        return ResponseEntity.ok(contactService.findAll());
    }

    @Operation(summary = "Criar novo contato", description = "Cria um novo contato no HubSpot com os dados fornecidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contato criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonNode.class))),
        @ApiResponse(responseCode = "400", description = "Dados do contato inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<JsonNode> createContact(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do contato a ser criado",
                required = true,
                content = @Content(schema = @Schema(implementation = ContactRequestDTO.class))
            )
            @RequestBody @Valid ContactRequestDTO contactRequestDTO) throws JsonProcessingException {
        JsonNode response = this.contactService.createContact(
                contactRequestDTO.getEmail(),
                contactRequestDTO.getFirstName(),
                contactRequestDTO.getLastName()
        );
        return ResponseEntity.ok(response);
    }
}
