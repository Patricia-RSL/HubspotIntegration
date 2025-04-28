# HubSpot Integration Project

Este projeto é uma aplicação Spring Boot para integração com a API do HubSpot, incluindo processamento de webhooks e gerenciamento de contatos.  
Uma documentação técnica detalhada está disponível [aqui](documentacaotecnica.md).

## Pré-requisitos

Certifique-se de ter os seguintes itens instalados em sua máquina:

- **Java 17** ou superior
- **Gradle**
- **Banco de Dados H2** (configurado no `application.properties`)
- **Ngrok** (para expor a aplicação local e receber webhooks)

## Configuração do Projeto

1. **Instale as dependências necessárias**: Java 17, Gradle e Ngrok.

2. **Configure o Ngrok** para redirecionar requisições externas:
    ```bash
    ngrok http 8080
    ```
   - Copie a URL pública gerada (ex.: `https://<subdomínio>.ngrok-free.app`) para configurar o HubSpot.

3. **Configure seu aplicativo no HubSpot**:
   - Adicione a URL do Ngrok com o caminho `/auth/callback` na aba de autenticação:
     ```text
     Exemplo: https://<subdomínio>.ngrok-free.app/auth/callback
     ```
   - Adicione os escopos `crm.objects.contacts.read` e `crm.objects.contacts.write`.
   - Após criar o aplicativo, copie o **ID do cliente** e o **Segredo do cliente**.

4. **Configure Webhooks no HubSpot**:
   - Adicione a URL do Ngrok com o caminho `/webhook/webhook/contact-creation`:
     ```text
     Exemplo: https://<subdomínio>.ngrok-free.app/webhook/webhook/contact-creation
     ```
   - Ative a assinatura para eventos de `contact.creation`.

5. **Atualize o arquivo `application.properties`**:
    ```properties
    jasypt.encryptor.password=<sua-senha>
    hubspot.client.id=<id-do-cliente>
    hubspot.client.secret=<segredo-do-cliente>
    hubspot.redirect.uri=https://<subdomínio>.ngrok-free.app/auth/callback
    ```

6. **Inicie a aplicação** em sua IDE favorita ou com o Gradle:
    ```bash
    ./gradlew bootRun
    ```

## Autenticação no HubSpot

Antes de criar um contato, é necessário obter o token de acesso:

1. Faça uma requisição `GET` para o endpoint `/auth/url`.
```bash
curl --location 'http://localhost:8080/auth/url'
```
2. Acesse a URL retornada e clique em "Permitir acesso"
3. Se tudo estiver correto, você verá uma mensagem de sucesso como abaixo e o token de acesso será salvo no banco de dados H2 automaticamente
> **Atenção**: Como o banco H2 é em memória, o token será perdido ao reiniciar a aplicação. Para persistência, utilize um banco como MySQL ou PostgreSQL.

![img.png](img.png)


## Criando um Contato

Com o token de acesso salvo, envie uma requisição `POST` para `/contact/` com o corpo no formato JSON:

```bash 
   curl --location 'http://localhost:8080/contact' \
   --header 'Content-Type: application/json' \
   --data-raw '{
   "email": "irvy@lumon.industries",
   "lastName": "J.",
   "firstName": "Irvy"
   }'

```
A aplicação incluirá automaticamente o token no cabeçalho da requisição para o HubSpot.
Os contatos criados podem ser verificados diretamente no HubSpot ou listados pelo endpoint /contact/.

``` bash
curl --location 'http://localhost:8080/contact'
```

## Webhook

Ao receber um webhook de criação de contato, será possível visualizar um log no console da aplicação.Exemplo:

``` bash
2025-04-28T15:34:46.401-03:00  INFO 46216 --- [HubspotIntegration] [nio-8080-exec-9] c.e.h.c.HubSpotWebhookController         : Recebido evento do webhook: [HubSpotWebhookEvent(appId=123, eventId=100, subscriptionId=3558218, portalId=49704295, occurredAt=1745861475897, subscriptionType=contact.creation, attemptNumber=0, objectId=123, changeSource=CRM, changeFlag=NEW)]
2025-04-28T15:34:46.402-03:00  INFO 46216 --- [HubspotIntegration] [nio-8080-exec-9] c.e.h.service.ContactService             : Processando evento de criação de contato: HubSpotWebhookEvent(appId=123, eventId=100, subscriptionId=3558218, portalId=49704295, occurredAt=1745861475897, subscriptionType=contact.creation, attemptNumber=0, objectId=123, changeSource=CRM, changeFlag=NEW)

```