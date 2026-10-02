# Especificação 03 — Especificação Técnica do Backend
## Stack: Kotlin 2.x + Spring Boot 3.3+ | TLC Spec-Driven Development

---

## 1. Visão Geral do Módulo Backend

O backend do AdotaMatch provê serviços RESTful, gerencia transações com o banco relacional e encapsula o motor de recomendação híbrido. Segue rigorosamente a [Constituição do Backend](file:///c:/Users/gerso/OneDrive/Documentos/TCC/adotamatch/backend/constitution.md) e as convenções de tipagem e nomenclatura do projeto.

---

## 2. Estrutura de Pacotes (`br.com.adotamatch`)

* `controller`:
  * `CPetController`: CRUD e listagem de animais sob custódia.
  * `CAdopterController`: Cadastro e atualização do perfil do adotante.
  * `CRecommendationController`: Endpoint principal de execução da compatibilidade e ranqueamento.
  * `CShelterController`: Gestão da ONG/abrigo e relatórios de triagem.
* `service`:
  * `CPetService`: Regras de negócio de cadastro, disponibilidade e validações de animais.
  * `CAdopterService`: Gestão de adotantes e seus perfis socioespaciais.
  * `CRecommendationService`: Orquestração do pipeline de recomendação.
  * `recommendation/CHardConstraintsFilter`: Avaliação booleana eliminatória $R(u, a)$.
  * `recommendation/CWeightedScorer`: Cálculo matemático da pontuação ponderada.
  * `recommendation/CLlmSemanticService`: Invocação e tratamento de respostas da LLM.
* `domain`:
  * `CPet`: Entidade JPA representando o animal.
  * `CPetBehavioralLog`: Entidade JPA com dados comportamentais e histórico do abrigo.
  * `CAdopter`: Entidade JPA do adotante.
  * `CAdopterProfile`: Entidade JPA do perfil habitacional e rotina.
  * `CShelter`: Entidade JPA da instituição mantenedora.
  * `CAdoptionMatch`: Registro de resultado de compatibilização.
* `dto`:
  * `request/CAdopterProfileRequest`: Payload com variáveis estruturadas e texto livre do adotante.
  * `request/CPetCreateRequest`: Payload para cadastramento de novos animais.
  * `response/CRecommendationResponse`: DTO com o animal, escore global, escore semântico, fatores de convergência e alertas preventivos.
  * `response/CPetSummaryResponse`: DTO resumido para listagens e catálogos.
* `client`:
  * `ILlmClient`: Interface para desacoplar o provedor de LLM.
  * `CSpringAiLlmClient`: Implementação utilizando Spring AI.
  * `CHeuristicFallbackLlmClient`: Implementação de contingência (fallback determinístico).

---

## 3. Contratos de API RESTful (Endpoints Primários)

### 3.1 Geração de Recomendações
* **Método/Rota:** `POST /api/v1/recommendations/match`
* **Descrição:** Executa a compatibilização completa de um adotante contra todos os animais ativos disponíveis.
* **Payload de Entrada (`CAdopterProfileRequest`):**
```json
{
  "speciesPreference": "DOG",
  "housingType": "APARTMENT",
  "dailyHoursAvailable": 4,
  "hasChildren": true,
  "hasOtherPets": false,
  "experienceLevel": 2,
  "lifestyleNarrative": "Trabalho em regime híbrido, moro em apartamento amplo telado. Procuro um cão dócil que conviva bem com meu filho de 6 anos e suporte períodos moderados sozinho."
}
```

* **Payload de Resposta (`List<CRecommendationResponse>`):**
```json
[
  {
    "petId": 104,
    "petName": "Pipoca",
    "species": "DOG",
    "size": "MEDIUM",
    "compatibilityScore": 88.5,
    "semanticScore": 0.90,
    "alignmentFactors": [
      "Perfil calmo e tolerante com crianças pequenas, conforme histórico do abrigo.",
      "Demanda de passeios e energia (nível 2) perfeitamente alinhada à disponibilidade declarada de 4 horas diárias."
    ],
    "preventiveWarnings": [
      "Exige enriquecimento ambiental nos primeiros meses para adaptação à permanência solitária.",
      "Sensível a ruídos intensos (fogos de artifício), demandando suporte da família em dias chuvosos ou festividades."
    ],
    "shelterContact": {
      "shelterName": "Associação Amigos dos Bichos",
      "city": "Ubá",
      "state": "MG"
    }
  }
]
```

---

## 4. Estratégia de Fallback e Resiliência da LLM

```kotlin
/**
 * @description Executa a avaliação semântica e explicativa com proteção de timeout e fallback determinístico.
 * @param pAdopterNarrative Relato textual de rotina e expectativas do adotante.
 * @param pPetBehavioralLog Registro comportamental preenchido pelo abrigo.
 * @returns Resultado estruturado de alinhamento e justificativas.
 */
fun evaluateSemanticCompatibility(
    pAdopterNarrative: String,
    pPetBehavioralLog: String
): CLlmEvaluationResult {
    return try {
        llmClient.callStructuredPrompt(pAdopterNarrative, pPetBehavioralLog)
    } catch (pEx: Exception) {
        logger.warn("Falha ou timeout na comunicação com a LLM. Acionando fallback heurístico.", pEx)
        fallbackClient.generateRuleBasedEvaluation(pAdopterNarrative, pPetBehavioralLog)
    }
}
```
