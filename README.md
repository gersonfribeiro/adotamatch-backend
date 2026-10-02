# AdotaMatch — Backend REST API
> **Sistema Inteligente de Recomendação Híbrida e Explicável para Adoção Responsável de Animais**

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.20-7F52FF.svg?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-6DB33F.svg?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36.svg?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![OpenAPI](https://img.shields.io/badge/OpenAPI-Swagger_UI-85EA2D.svg?logo=swagger&logoColor=black)](http://localhost:8080/swagger-ui.html)
[![TLC Spec-Driven](https://img.shields.io/badge/Methodology-TLC_Spec--Driven-blue.svg)](#metodologia-tlc-spec-driven)

---

## 1. Visão Geral do Projeto

O **AdotaMatch Backend** provê os serviços computacionais do ecossistema AdotaMatch, cujo objetivo central é a **mitigação do reabandono, dos maus-tratos e da devolução traumática de cães e gatos** sob custódia de abrigos e organizações não governamentais (ONGs).

Diferente de sistemas tradicionais que apenas filtram animais por atributos rígidos em consultas de banco de dados, o AdotaMatch implementa um **Motor de Recomendação Híbrido em Múltiplos Estágios** que combina:
1. **Camada de Restrições Rígidas ($R(u, a) \in \{0, 1\}$):** Filtro eliminatório booleano para incompatibilidades intransponíveis.
2. **Camada de Pontuação Ponderada Multicritério ($s_c(u, a)$):** Cálculo de similaridade vetorial para variáveis físicas, espaciais e de rotina.
3. **Inferência Semântica e Contextual via LLM ($S_{\text{LLM}}(u, a)$):** Extração de nuances de relatos em texto livre do adotante e diários comportamentais do abrigo.
4. **Módulo de Explicabilidade Preventiva:** Geração em linguagem natural de *Fatores de Afinidade* e *Alertas de Manejo e Desafios* antes da efetivação da tutela.

---

## 2. Metodologia TLC Spec-Driven Development

Este repositório é governado pelo paradigma **Spec-Driven**:
* 📜 **[constitution.md](constitution.md):** Princípios inegociáveis, convenções de código (prefixo `C` para classes, `I` para interfaces, `p` para parâmetros) e governança arquitetural.
* 🤖 **[agents.md](agents.md):** Catálogo de papéis e diretrizes para atuação de Agentes de IA.
* 📋 **Especificações Técnicas e de Negócio (`specs/`):**
  * `01-business-domain-spec.md`: Requisitos de negócio, atores e modelo conceitual.
  * `02-system-architecture-spec.md`: Decisões de Arquitetura (ADRs) e fluxos de dados.
  * `03-backend-technical-spec.md`: Contratos de API RESTful, payloads e persistência.
  * `05-recommendation-engine-spec.md`: Formalização matemática das funções de similaridade e engenharia de prompts.

---

## 3. Padrão Arquitetural

* **Vertical Slice by Feature + DDD + MVC Convencional:**
  * O código é estruturado por capacidades de negócio (`features/recommendation`, `features/pet`, `features/adopter`, `shared`), garantindo alta coesão e baixo acoplamento.
  * Cada fatia implementa o padrão MVC direto (`controller`, `service`, `domain`, `dto`, `repository`), sem sobrecarga de Clean Architecture com dezenas de interfaces vazias.
  * **O Core Domain é o Sistema de Recomendação:** As operações de CRUD são secundárias e enxutas.

```txt
src/main/kotlin/br/com/adotamatch/
├── features/
│   ├── recommendation/          # [CORE DOMAIN DA APLICAÇÃO]
│   │   ├── controller/          # CRecommendationController.kt
│   │   ├── service/             # CRecommendationService.kt, CHardConstraintsFilter.kt, CWeightedScorer.kt
│   │   ├── client/              # ILlmClient.kt, CHeuristicFallbackClient.kt
│   │   └── dto/                 # CRecommendationRequest.kt, CRecommendationResponse.kt
│   ├── pet/                     # [FEATURE DE ANIMAIS E CUSTÓDIA]
│   │   ├── domain/              # CPet.kt (JPA Entity, Enums)
│   │   └── repository/          # IPetRepository.kt
│   └── adopter/                 # [FEATURE DE ADOTANTES]
├── shared/                      # [COMPARTILHADOS]
│   ├── config/                  # CCorsConfig.kt
│   └── exception/               # CGlobalExceptionHandler.kt (Problem Details RFC 7807)
└── AdotaMatchApplication.kt     # Ponto de entrada Spring Boot
```

---

## 4. Requisitos e Pré-requisitos

* **Java JDK:** Versão 21 ou 22 LTS.
* **Apache Maven:** Versão 3.9+.

---

## 5. Como Executar

### 5.1 Executar a Bateria de Testes Unitários
```bash
mvn clean test
```

### 5.2 Inicializar a Aplicação
```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

* **Swagger UI Interativo:** `http://localhost:8080/swagger-ui.html`
* **Especificação OpenAPI JSON:** `http://localhost:8080/api-docs`
* **Console H2 Database:** `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:adotamatchdb`)

---

## 6. Exemplo de Requisição do Core de Recomendação

### Endpoint: `POST /api/v1/recommendations/match`

**Payload de Entrada:**
```json
{
  "speciesPreference": "DOG",
  "housingType": "HOUSE_YARD",
  "dailyHoursAvailable": 4,
  "hasChildren": true,
  "hasOtherPets": false,
  "experienceLevel": 2,
  "lifestyleNarrative": "Trabalho em home office três dias por semana, gosto de passeios moderados e procuro um cão dócil que conviva bem com meu filho de 6 anos."
}
```

**Resposta do Motor Híbrido:**
```json
[
  {
    "petId": 1,
    "petName": "Caramelo",
    "species": "DOG",
    "size": "MEDIUM",
    "compatibilityScore": 86.5,
    "semanticScore": 0.85,
    "alignmentFactors": [
      "Sua rotina flexível ou trabalho remoto oferece a presença constante ideal para o temperamento de Caramelo.",
      "Seu interesse declarado por passeios diários harmoniza-se com a energia ativa de Caramelo.",
      "O animal possui histórico positivo de convivência e paciência com crianças no abrigo."
    ],
    "preventiveWarnings": [
      "Como em qualquer adoção, reserve os primeiros 15 a 30 dias para a fase de adaptação e criação de vínculo."
    ],
    "shelterName": "ONG Amor de Patas",
    "shelterCity": "Ubá"
  }
]
```

---

## 7. Licença

Este projeto é desenvolvido para fins acadêmicos e sociais no âmbito do Bacharelado em Ciência da Computação do **UNIFAGOC**.
