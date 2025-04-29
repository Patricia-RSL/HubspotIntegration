package com.example.hubspotintegration.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração para o RabbitMQ.
 * Esta classe configura a estrutura de mensageria utilizada para processamento
 * assíncrono de eventos recebidos do HubSpot, incluindo:
 * - Exchange para roteamento de mensagens
 * - Filas para diferentes tipos de eventos
 * - Conversor de mensagens JSON
 * - Template para envio de mensagens
 */
@Configuration
public class RabbitConfig {

    /**
     * Nome da exchange utilizada para roteamento de mensagens de webhook.
     */
    public static final String EXCHANGE_NAME = "webhook_exchange";
    public static final String QUEUE_CONTACT_CREATE = "contact_create_queue";

    /**
     * Configura a exchange do tipo Direct para roteamento de mensagens.
     * 
     * @return DirectExchange configurada
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    /**
     * Configura a fila para processamento de eventos de criação de contatos.
     * A fila é configurada como durável para garantir que as mensagens
     * não sejam perdidas em caso de reinicialização do servidor.
     * 
     * @return Queue configurada
     */
    @Bean
    public Queue contactCreateQueue() {
        return new Queue(QUEUE_CONTACT_CREATE, true);
    }

    /**
     * Configura o binding entre a fila de criação de contatos e a exchange.
     * As mensagens com a routing key "contact.creation" serão roteadas
     * para a fila de criação de contatos.
     * 
     * @param contactCreateQueue Fila de criação de contatos
     * @param exchange Exchange direta
     * @return Binding configurado
     */
    @Bean
    public Binding bindingCreate(Queue contactCreateQueue, DirectExchange exchange) {
        return BindingBuilder.bind(contactCreateQueue).to(exchange).with("contact.creation");
    }

    /**
     * Configura o conversor de mensagens para formato JSON.
     * Este conversor é utilizado para serializar e desserializar
     * as mensagens trocadas entre os componentes do sistema.
     * 
     * @return Jackson2JsonMessageConverter configurado
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configura o template do RabbitMQ com o conversor JSON.
     * Este template é utilizado para enviar mensagens para as filas
     * e é injetado nos serviços que precisam publicar eventos.
     * 
     * @param connectionFactory Fábrica de conexões do RabbitMQ
     * @param converter Conversor de mensagens JSON
     * @return RabbitTemplate configurado
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
