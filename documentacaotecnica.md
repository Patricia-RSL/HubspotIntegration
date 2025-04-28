# Documentação Técnica

## Decisões e Motivações

### Frameworks e Bibliotecas
- **Spring Boot**: Escolhido por sua simplicidade e um ecossistema robusto, que facilita o desenvolvimento rápido de APIs REST e a integração com serviços externos, como o HubSpot.
- **Lombok**: Utilizado para reduzir código repetitivo, como getters, setters e construtores, melhorando a legibilidade e a manutenção do código.
- **Jackson**: Adotado para a serialização e desserialização de JSON, simplificando o tratamento de solicitações e respostas de APIs.
- **Ngrok**: Integrado para expor a aplicação local à internet, possibilitando o recebimento de webhooks sem a necessidade de um servidor remoto.
- **H2 Database**: Escolhido como banco de dados em memória para desenvolvimento, proporcionando uma solução leve e rápida sem configuração adicional.
- **Jasypt**: Utilizado para criptografar tokens, aumentando a segurança.

## Decisões de Design
- **Camada de Serviço**: Introduzida para encapsular a lógica de negócios, garantindo uma separação clara de responsabilidades e facilitando a manutenção e expansão do código.
- **Tratamento de Erros**: Centralizado em uma classe `@ControllerAdvice` chamada `GlobalExceptionHandler`, que captura exceções globais da aplicação. Essa abordagem garante respostas consistentes e informativas para os clientes, independentemente do ponto em que o erro ocorre.
- **Processamento de Webhooks**: Inicialmente, registra eventos de webhook recebidos para depuração e validação, sem nenhum processamento real.

## Melhorias Futuras
Embora o foco deste projeto seja entregar uma solução rápida e funcional para atender às exigências do desafio técnico, algumas melhorias podem ser implementadas para um ambiente de produção:

1. **Entidade Usuário**: Introduzir a entidade **Usuário** para gerenciar tokens. Cada token seria associado a um usuário específico, e seria necessário implementar um fluxo de autenticação. O usuário precisaria fazer login no sistema para obter ou utilizar um token válido.
2. **Autenticação com Spring Security**: Implementar autenticação e autorização robustas, protegendo os endpoints da API. Um exemplo de implementação desenvolvido por mim está disponível neste [repositório no GitHub](https://github.com/Patricia-RSL/carteiraInvestimentos).
3. **Banco de dados persistente**: Substituir o H2 por uma solução como MySQL ou PostgreSQL para persistência de dados em longo prazo, permitindo o armazenamento seguro de usuários e tokens.
4. **Filas de mensagens**: Implementar RabbitMQ ou Kafka para processar eventos de webhook de forma assíncrona, melhorando o desempenho.
5. **Lógica para eventos de criação de contatos**: Adicionar funcionalidades como envio de notificações ou sincronização de dados com outros sistemas.

Com essas melhorias, o sistema estaria preparado para um ambiente de produção, atendendo às necessidades de segurança, escalabilidade e funcionalidade.
