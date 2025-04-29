# Documentação Técnica

## Decisões e Motivações

### Frameworks e Bibliotecas
- **Spring Boot**: Permite o desenvolvimento rápido de APIs REST e integração com serviços externos, como o HubSpot.
- **Lombok**: Reduz código repetitivo (e.g., getters, setters, construtores), melhorando a legibilidade e a manutenção do código.
- **OpenFeign**: Simplifica a comunicação com APIs externas, como a API do HubSpot
- **Spring Data JPA**: Facilita a interação e mapeamento com o banco de dados PostgreSQL.
- **Ngrok**: Expõe a aplicação local ao Ngrok sem fazer deploy, permitindo o recebimento de webhooks.
- **Jasypt**: Usado para criptografar tokens, aumentando a segurança da aplicação.
- **PostgreSQL**: Banco de dados para armazenamento de tokens.
- **RabbitMQ**: Broker de mensagens para processar eventos de webhook de forma assíncrona, melhorando o desempenho e desacoplando componentes.
- **Jackson**: Adotado para serialização/deserialização de JSON no momento de inserir e consumir webhooks da fila.

## Decisões de Design
- **Arquitetura em Camadas**: A aplicação é dividida em camadas (Controller, Service, Repository) para separar responsabilidades e facilitar a manutenção.
- **Observer Pattern**: Através do RabbitMq, é possível que a aplicação reaja a eventos sem bloquear o fluxo principal.

## Melhorias Futuras
1. **Entidade Usuário**: Adicionar uma entidade **Usuário** para gerenciar tokens, associando cada token a um usuário específico.
2. **Autenticação com Spring Security**: Implementar um fluxo de autenticação e autorização que exija login para acessar ou usar tokens.
3. **Lógica de Webhooks**: Implementar o processamento de webhooks, como envio de notificações ou sincronização de dados com outros sistemas.
4. **Expansão dos webhooks**: Adicionar suporte a outros eventos de webhook do HubSpot, como exclusão de contatos.