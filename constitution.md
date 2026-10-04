# Constituição do Repositório Backend — AdotaMatch
## Stack: Kotlin 2.x + Spring Boot 3.3+ (Maven) | TLC Spec-Driven Development

---

## 1. Visão Geral, Ontologia e Missão Social

O backend do **AdotaMatch** é desenvolvido em **Kotlin 2.x** com **Spring Boot 3.3+** gerenciado pelo **Maven**, concebido sob as diretrizes do Trabalho de Conclusão de Curso em Ciência da Computação:
> **"ADOTAMATCH: SISTEMA DE RECOMENDAÇÃO BASEADO EM CONTEÚDO E MODELOS DE LINGUAGEM DE GRANDE PORTE (LLMs) PARA APOIO À ADOÇÃO RESPONSÁVEL DE ANIMAIS"** (UNIFAGOC, 2026).

A razão primordial do software é a **redução drástica do reabandono, dos maus-tratos e da devolução traumática de animais acolhidos por ONGs e protetores**, por meio de um cruzamento algorítmico rigoroso entre o perfil socioambiental do adotante e as demandas comportamentais do pet.

---

## 2. Cláusulas Pétreas do TCC no Backend

Fica expressamente vedado a qualquer desenvolvedor ou agente de IA descumprir as seguintes cláusulas pétreas:

1. **O Core Domain é o Sistema de Recomendação Híbrido:**
   O coração da aplicação reside no motor de compatibilidade e explicabilidade (`features/recommendation`). As operações de CRUD para animais e adotantes são utilitárias, enxutas e secundárias.
2. **Proibição de Busca Booleana Estática Simples:**
   O AdotaMatch não é um catálogo de anúncios. É terminantemente proibido substituir o motor de recomendação por meras consultas `WHERE` em banco relacional. A ordenação deve obedecer à função multicritério convexa combinada à inferência semântica de LLMs.
3. **Padrão Arquitetural Estrito (Vertical Slice by Feature + DDD Pragmático + MVC Convencional):**
   * **Sem sobrecarga de Clean Architecture:** Não fragmentar o código em camadas burocráticas infinitas (`UseCases`, `Gateways`, `Presenters` vazios com dezenas de interfaces desnecessárias).
   * **MVC Convencional em cada fatia:** Dentro de cada slice (`features/recommendation`, `features/pet`, `features/adopter`), adota-se o padrão direto: `Controller` $\rightarrow$ `Service` $\rightarrow$ `Repository` / `Client` / `Domain Entity` / `DTO`.
4. **Fórmula Matemática Canônica do TCC:**
   A compatibilidade global deve seguir rigorosamente a formulação descrita na monografia:
   $$\text{Score}(u, a) = 100 \times R(u, a) \times \left[ \sum_{c=1}^5 w_c \cdot s_c(u, a) + w_{\text{LLM}} \cdot S_{\text{LLM}}(u, a) \right]$$
   Onde $R(u, a) \in \{0, 1\}$ é o filtro de restrições rígidas, $\sum w_c + w_{\text{LLM}} = 1,0$ (pesos convexos normalizados) e $S_{\text{LLM}}(u, a) \in [0, 1]$ é o alinhamento semântico contextual.
5. **Resiliência e Fallback Heurístico Obrigatório:**
   Nenhum cálculo de compatibilidade pode falhar ou travar devido à indisponibilidade de APIs externas de LLM. Em caso de *timeout*, erro de rede ou resposta malformada da LLM, o backend deve acionar automaticamente o `CHeuristicFallbackClient`, calculando a pontuação com base em regras heurísticas determinísticas e justificativas pré-formatadas, garantindo 100% de disponibilidade.
6. **Segurança de IA e Proteção contra Prompt Injection:**
   Todo texto livre submetido por usuários ($T_u$ e $T_a$) deve ser sanitizado, desprovido de comandos que alterem as instruções do sistema, truncado em no máximo 1000 caracteres e passado estritamente via delimitadores contextuais na API de LLM.

---

## 3. Padrão de Organização de Pastas (Vertical Slice by Feature)

A estrutura de código em `src/main/kotlin/br/com/adotamatch` reflete fielmente as capacidades de negócio:

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
├── shared/                        # [INFRAESTRUTURA E RECURSOS TRANSVERSAIS]
│   ├── config/                    # CCorsConfig.kt, CSwaggerConfig.kt, CLlmConfig.kt
│   ├── exception/                 # CGlobalExceptionHandler.kt, CDomainException.kt, CResourceNotFoundException.kt
│   └── constants/                 # CRecommendationConstants.kt, CWeightConstants.kt
└── AdotaMatchApplication.kt       # Ponto de entrada Spring Boot
```

---

## 4. Convenções Estritas de Nomenclatura e Código em Kotlin

### 4.1 Prefixos e Formatação
* **Classes:** Iniciam obrigatoriamente com o prefixo `C` (ex.: `CRecommendationController`, `CPetService`, `CPet`, `CSpringAiLlmClient`).
* **Interfaces:** Iniciam obrigatoriamente com o prefixo `I` (ex.: `IPetRepository`, `ILlmClient`, `IAdopterRepository`).
* **Constantes:** Grafadas em caixa alta com sublinhado (`UPPER_CASE`) (ex.: `WEIGHT_ENERGY_TIME`, `DEFAULT_PAGE_SIZE`, `MAX_TEXT_LENGTH`).
* **Atributos e Métodos:** Utilizam `camelCase` (ex.: `dailyHoursAvailable`, `calculateScore()`).
* **Parâmetros de Funções:** Devem iniciar obrigatoriamente com o prefixo `p` e possuir tipagem forte e explícita:
  ```kotlin
  fun calculateHardConstraints(pAdopter: CAdopter, pPet: CPet): Boolean {
      // ...
  }
  ```

### 4.2 Documentação Interna (KDoc em Bloco Único)
* A documentação de classes, interfaces e métodos públicos deve usar bloco único de KDoc imediatamente acima do símbolo.
* Para métodos, utilizar:
  * `@description` ou texto direto explicando a responsabilidade.
  * `@param pNome` para cada parâmetro.
  * `@returns` descrevendo o valor retornado.

```kotlin
/**
 * Serviço responsável pelo cálculo de compatibilidade e orquestração da recomendação híbrida.
 */
@Service
class CRecommendationService(
    private val pPetRepository: IPetRepository,
    private val pHardConstraintsFilter: CHardConstraintsFilter,
    private val pWeightedScorer: CWeightedScorer,
    private val pLlmClient: ILlmClient
) {
    /**
     * Gera a lista ranqueada de animais compatíveis para o adotante informado.
     * @param pRequest Dados do perfil socioambiental e relato livre do adotante.
     * @returns Lista de recomendações com score ponderado, fatores de afinidade e alertas preventivos.
     */
    fun match(pRequest: CRecommendationRequest): List<CRecommendationResponse> {
        // ...
    }
}
```

### 4.3 Imutabilidade e Boas Práticas do Kotlin
* DTOs e Value Objects devem ser declarados como `data class` com propriedades imutáveis (`val`).
* Nunca expor entidades de banco de dados (`CPet`, `CAdopter`) diretamente na camada de Controller. O tráfego externo deve ser realizado exclusivamente via DTOs tipados.
* Tratamento centralizado de exceções via `@RestControllerAdvice`, emitindo respostas estruturadas de erro (RFC 7807 Problem Details).
* Testes unitários obrigatórios para todas as funções de similaridade matemática $s_c(u, a)$ e regras de restrições rígidas $R(u, a)$.
