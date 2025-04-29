package com.example.hubspotintegration.integration;

import com.example.hubspotintegration.component.HubSpotFeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * Cliente Feign para interação com a API de contatos do HubSpot.
 * Este cliente é responsável por realizar operações CRUD de contatos
 * através da API do HubSpot
 * 
 * O cliente utiliza um interceptor para adicionar o token de acesso
 * nas requisições de forma automática.
 */
@FeignClient(name = "hubspotContact",
        url = "https://api.hubapi.com",
        configuration = HubSpotFeignInterceptor.class)
public interface HubSpotContactClient {

    /**
     * Consulta todos os contatos cadastrados no HubSpot.
     * Este método faz uma requisição GET para a API de contatos do HubSpot
     * e retorna a lista completa de contatos em formato JSON.
     *
     * @return String contendo a lista de contatos em formato JSON
     */
    @GetMapping("/crm/v3/objects/contacts")
    String fetchContacts();

    /**
     * Cria um novo contato no HubSpot.
     * Este método faz uma requisição POST para a API de contatos do HubSpot
     * com as propriedades do contato a ser criado.
     *
     * @param properties Mapa contendo as propriedades do contato, incluindo
     *                  email, nome e sobrenome
     * @return String contendo os dados do contato criado em formato JSON
     */
    @PostMapping("/crm/v3/objects/contacts")
    String createContact(@RequestBody Map<String, Object> properties);
}

