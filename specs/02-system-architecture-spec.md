# Especificação 02 — Arquitetura Geral do Sistema
## Projeto AdotaMatch — TLC Spec-Driven Development

---

## 1. Visão Macro da Arquitetura

O **AdotaMatch** adota o paradigma de **Arquitetura Desacoplada Cliente-Servidor orientada a Serviços RESTful e SPA (Single Page Application)**. 

Esse arranjo assegura total independência entre a camada de apresentação visual (Frontend) e o motor de regras de negócio, persistência e orquestração de Inteligência Artificial (Backend).

```mermaid
flowchart TD
    subgraph ClientLayer["Camada do Cliente (Frontend SPA)"]
        UI["Vue 3 + PrimeVue + Tailwind"]
        Pinia["Stores Pinia (Estado Global)"]
        Router["Vue Router (Navegação SPA)"]
        Axios["CPetService / CRecommendationService (Axios)"]
    end

    subgraph APILayer["Camada de Serviços (Backend Spring Boot)"]
        direction TB
        Ctrl["Controllers REST (Spring MVC / OpenAPI)"]
        Sec["Security & CORS Filter"]
        Val["Bean Validation (Jakarta)"]
        Srv["Camada de Serviços (Kotlin / Spring Data JPA)"]
        Engine["Motor de Recomendação Híbrido"]
    end

    subgraph DataAndAI["Camada de Persistência e Inteligência Artificial"]
        DB[("PostgreSQL Database")]
        LLM["Provedor LLM (OpenAI / Anthropic / Gemini / Ollama)"]
    end

    UI <--> Pinia
    UI <--> Router
    Pinia <--> Axios
    Axios <== "HTTPS / JSON (REST API)" ==> Sec
    Sec --> Ctrl
    Ctrl --> Val
    Val --> Srv
    Srv --> DB
    Srv --> Engine
    Engine <--> LLM
```

---

## 2. Decisões Arquiteturais Fundamentais (ADRs)

### ADR 01: Adoção de SPA (Vue 3) desacoplada de API RESTful (Kotlin / Spring Boot)
* **Contexto:** Necessidade de permitir uma experiência interativa, reativa e sem recargas completas de tela para preenchimento de perfis e navegação por animais recomendados, facilitando futuras versões mobile (PWA ou aplicativo nativo).
* **Decisão:** O backend atua exclusivamente como provedor de APIs RESTful sem renderização de páginas no servidor (sem Thymeleaf/JSP).
* **Consequência:** Separação estrita de preocupações e possibilidade de evolução independente das interfaces.

### ADR 02: Motor Híbrido com Fallback Heurístico para LLMs
* **Contexto:** LLMs comerciais e modelos abertos podem sofrer com latência variável de rede, custos de token ou indisponibilidade de API.
* **Decisão:** O cálculo base e o descarte por restrições rígidas ocorrem deterministicamente no backend. A chamada à LLM é executada de forma assíncrona com timeout rigoroso. Caso a LLM não responda em até 5 segundos, ativa-se o módulo de fallback baseado em regras predefinidas, garantindo a entrega da recomendação.
* **Consequência:** Alta resiliência operacional (100% de disponibilidade da função principal do sistema).

### ADR 03: Governança Estrita de Tipos e Contratos
* **Contexto:** Prevenção de regressões e falhas de integração entre frontend e backend.
* **Decisão:** Todos os payloads de requisição e resposta são documentados com especificações JSON / OpenAPI e convertidos em interfaces fortemente tipadas no frontend (`models/`) e DTOs imutáveis no backend (`dto/`).

---

## 3. Fluxo de Execução da Recomendação (Pipeline de Dados)

```mermaid
sequenceDiagram
    autonumber
    actor Adotante
    participant Front as Frontend (Vue 3 / PrimeVue)
    participant Back as Backend (Spring Boot API)
    participant DB as PostgreSQL
    participant AI as Provedor de LLM

    Adotante->>Front: Submete Perfil e Relato de Rotina
    Front->>Front: Ativa Lock de Requisição (Loading State)
    Front->>Back: POST /api/v1/recommendations/match (Payload DTO)
    Back->>DB: Consulta animais disponíveis sob custódia
    DB-->>Back: Lista de CPet ativos
    Back->>Back: Executa Camada 1: Restrições Rígidas R(u, a)
    Back->>AI: Envia par (T_u, T_a) com System Prompt e JSON Schema
    AI-->>Back: Retorna S_LLM(u, a) + Fatores de Convergência + Alertas
    Back->>Back: Executa Camada 2: Ponderação Multicritério Score(u, a)
    Back->>Back: Ordena Top-K e monta DTO de resposta
    Back-->>Front: Retorna lista ordenada com justificativas
    Front->>Front: Desativa Lock e renderiza Cards com Alertas Preventivos
    Front-->>Adotante: Exibe recomendações transparentes
```
