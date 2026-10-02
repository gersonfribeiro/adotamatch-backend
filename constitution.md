# Constituição do Repositório Backend — AdotaMatch
## Stack: Kotlin 2.x + Spring Boot 3.3+ (Maven) | Vertical Slice by Feature + DDD + MVC Convencional

---

## 1. Visão Geral e Propósito

O backend do **AdotaMatch** é desenvolvido em **Kotlin** com **Spring Boot** gerenciado pelo **Maven**, com foco primordial no **Core do Sistema de Recomendação Híbrido** e na prevenção do reabandono animal.

Em alinhamento às diretrizes do projeto:
* **Não utilizamos Clean Architecture complexa:** Não há separação burocrática em UseCases, Gateways e Presenters com dezenas de interfaces vazias.
* **Adotamos MVC Convencional:** Cada fatia vertical organiza-se de maneira direta e pragmática (Controller $\rightarrow$ Service/Model $\rightarrow$ DTO/Entity).
* **Estruturação em Vertical Slice By Feature + DDD:** O código organiza-se por domínios e capacidades de negócio (Features), mantendo alta coesão e baixo acoplamento.
* **O Core é a Recomendação Inteligente:** As operações de CRUD para animais e adotantes são secundárias e enxutas; o coração do sistema reside no cálculo de compatibilidade, no descarte por restrições rígidas e na explicabilidade via LLMs.

---

## 2. Padrão de Organização de Pastas (Vertical Slice by Feature)

A estrutura de código em `src/main/kotlin/br/com/adotamatch` é organizada por fatias verticais de negócio:

```txt
br.com.adotamatch/
├── features/
│   ├── recommendation/            # [CORE DOMAIN DA APLICAÇÃO]
│   │   ├── controller/            # CRecommendationController.kt
│   │   ├── service/               # CRecommendationService.kt, CHardConstraintsFilter.kt, CWeightedScorer.kt
│   │   ├── domain/                # CCompatibilityScore.kt, CSemanticAlignment.kt (Value Objects)
│   │   ├── dto/                   # CRecommendationRequest.kt, CRecommendationResponse.kt
│   │   └── client/                # ILlmClient.kt, CSpringAiLlmClient.kt, CHeuristicFallbackClient.kt
│   ├── pet/                       # [FEATURE DE ANIMAIS E ABRIGOS]
│   │   ├── controller/            # CPetController.kt
│   │   ├── service/               # CPetService.kt
│   │   ├── domain/                # CPet.kt, CPetBehavioralLog.kt (Entities e Enums)
│   │   ├── dto/                   # CPetResponse.kt, CPetCreateRequest.kt
│   │   └── repository/            # IPetRepository.kt
│   └── adopter/                   # [FEATURE DE ADOTANTES]
│       ├── controller/            # CAdopterController.kt
│       ├── service/               # CAdopterService.kt
│       ├── domain/                # CAdopter.kt, CAdopterProfile.kt
│       ├── dto/                   # CAdopterProfileRequest.kt
│       └── repository/            # IAdopterRepository.kt
├── shared/                        # [RECURSOS TRANSVERSAIS COMPARTILHADOS]
│   ├── config/                    # CCorsConfig.kt, CSwaggerConfig.kt
│   ├── exception/                 # CGlobalExceptionHandler.kt, CDomainException.kt
│   └── constants/                 # CRecommendationConstants.kt
└── AdotaMatchApplication.kt       # Ponto de entrada Spring Boot
```

---

## 3. Convenções de Código e Nomenclatura em Kotlin

1. **Nomenclatura Obrigatória:**
   * Classes: iniciam com `C` (ex.: `CRecommendationController`, `CPetService`, `CPet`).
   * Interfaces: iniciam com `I` (ex.: `IPetRepository`, `ILlmClient`).
   * Parâmetros de funções: iniciam com o prefixo `p` e possuem tipagem explícita (ex.: `fun match(pRequest: CRecommendationRequest): List<CRecommendationResponse>`).
   * Constantes: grafadas em `UPPER_CASE`.
2. **Imutabilidade e Segurança:**
   * DTOs e Value Objects devem ser declarados preferencialmente como `data class` com propriedades imutáveis (`val`).
   * Não expor entidades de banco de dados JPA diretamente na camada de Controller; o tráfego externo deve ser mediado por DTOs.
3. **Resiliência de IA:**
   * Toda chamada a provedores de LLM deve possuir tratamento de timeout e acionamento automático de fallback heurístico para manter a disponibilidade ininterrupta do cálculo de recomendação.
