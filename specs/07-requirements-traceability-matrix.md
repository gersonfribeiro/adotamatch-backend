# Especificação 07 — Levantamento de Requisitos e Matriz de Rastreabilidade
## Projeto AdotaMatch — TLC Spec-Driven Development

---

## 1. Visão Geral e Propósito

Esta especificação formaliza o levantamento de requisitos de software da plataforma **AdotaMatch**, estabelecendo a rastreabilidade bidirecional com o Trabalho de Conclusão de Curso em Bacharelado em Ciência da Computação (UNIFAGOC, 2026):
> **"ADOTAMATCH: SISTEMA DE RECOMENDAÇÃO BASEADO EM CONTEÚDO E MODELOS DE LINGUAGEM DE GRANDE PORTE (LLMs) PARA APOIO À ADOÇÃO RESPONSÁVEL DE ANIMAIS"** (Ribeiro, 2026).

A metodologia **TLC Spec-Driven Development** exige que todo requisito funcional ou não-funcional esteja estritamente ancorado nos objetivos da monografia, mapeado em contratos de API e refletido em componentes do frontend e classes do backend.

---

## 2. Requisitos Funcionais (RF)

| Identificador | Nome do Requisito | Descrição Detalhada | Prioridade |
|:---|:---|:---|:---:|
| **RF-01** | Diagnóstico de Perfil do Adotante | Coletar dados estruturados do adotante (tipo de moradia, espaço físico, horas diárias disponíveis, presença de crianças, outros animais, horas de ausência, experiência prévia) e relato livre em linguagem natural ($T_u$). | **Essencial** |
| **RF-02** | Cadastro Comportamental do Pet | Coletar atributos do animal (espécie, porte, idade estimada, nível de energia, tolerância à solidão, sociabilidade com crianças/pets, cuidados especiais de saúde) e diário de observação comportamental da ONG ($T_a$). | **Essencial** |
| **RF-03** | Filtragem por Restrições Rígidas | Executar a função booleana $R(u, a) \in \{0, 1\}$. Descartar imediatamente candidatos com incompatibilidades graves de segurança física ou sanitária antes do cálculo de similaridade ponderada. | **Essencial** |
| **RF-04** | Similaridade Ponderada Multicritério | Calcular a pontuação quantitativa estruturada $\sum_{c=1}^5 w_c \cdot s_c(u, a)$ com base nos 5 critérios convexos: Energia/Tempo ($0,20$), Crianças/Pets ($0,15$), Espaço/Porte ($0,15$), Solidão/Ausência ($0,15$) e Experiência/Cuidados ($0,15$). | **Essencial** |
| **RF-05** | Inferência Semântica Contextual (LLM) | Analisar a coerência semântica entre o relato livre do adotante ($T_u$) e o diário do pet ($T_a$), gerando o escore contextual $S_{\text{LLM}}(u, a) \in [0, 1]$. | **Essencial** |
| **RF-06** | Explicabilidade e Alertas Preventivos | Sintetizar via LLM uma resposta estruturada (JSON Schema) composta por uma lista de **Fatores de Afinidade** (motivos de sinergia) e **Alertas Preventivos de Adaptação** (desafios reais de rotina e manejo). | **Essencial** |
| **RF-07** | Fallback Heurístico Resiliente | Disparar automaticamente o motor heurístico determinístico caso o serviço de LLM atinja timeout ($\ge 3500\text{ms}$) ou apresente instabilidade, garantindo pontuação e justificativas operacionais ininterruptas. | **Essencial** |
| **RF-08** | Ranqueamento e Ordenação Top-K | Ordenar os animais avaliados pela pontuação global $\text{Score}(u, a)$ de forma decrescente e disponibilizar paginação ou corte dos melhores matches para o adotante. | **Essencial** |
| **RF-09** | Visualização e Consentimento Consciente | Apresentar o dossiê detalhado do animal com destaque para o percentual de compatibilidade, Fatores de Afinidade e Alertas Preventivos, exigindo ciência do adotante antes de prosseguir. | **Essencial** |
| **RF-10** | Manifestação de Interesse e Contato | Permitir ao adotante submeter a manifestação de interesse à ONG responsável, encaminhando o relatório de compatibilidade com os dados do match. | **Importante** |
| **RF-11** | Painel da ONG / Protetor | Disponibilizar área para ONGs cadastrarem animais, atualizarem diários de observação e consultarem a lista de interessados ranqueada por compatibilidade. | **Importante** |
| **RF-12** | Telemetria e Logs para Validação Científica | Registrar anonimamente os eventos de recomendação (scores, pesos aplicados, tempo de resposta, ativações de fallback) para permitir a validação experimental no TCC 2. | **Desejável** |

---

## 3. Requisitos Não-Funcionais (RNF)

| Identificador | Categoria | Descrição e Métrica de Aceitação |
|:---|:---|:---|
| **RNF-01** | Performance & Latência | O processamento do cálculo híbrido com LLM para os Top-K animais não deve exceder $3.500\text{ms}$. Em modo de Fallback Heurístico, a latência de cálculo deve ser inferior a $200\text{ms}$. |
| **RNF-02** | Disponibilidade & Resiliência | A API de recomendação deve atingir disponibilidade operacional de 99,9%, garantida pela transição automática e imperceptível para o Fallback Heurístico. |
| **RNF-03** | Segurança de IA & Prompt Injection | Textos livres submetidos por adotantes ($T_u$) e protetores ($T_a$) devem sofrer sanitização de caracteres especiais, truncamento estrito em 1.000 caracteres e isolamento em delimitadores contextuais no System Prompt. |
| **RNF-04** | Privacidade e Conformidade LGPD | Nenhum dado de identificação pessoal direta do adotante (CPF, telefone, nome completo) deve ser trafegado nos prompts enviados aos provedores de LLM. Apenas vetores contextuais anonimizados são processados. |
| **RNF-05** | Usabilidade & Acessibilidade | A interface frontend deve atender aos requisitos de contraste WCAG 2.1 nível AA ($\ge 4,5:1$), alvos de toque mínimos de $44 \times 44\text{px}$ e atingir escore mínimo $\ge 75$ no teste padronizado SUS (*System Usability Scale*). |
| **RNF-06** | Responsividade Mobile-First | Toda a navegação e preenchimento de formulários deve ser perfeitamente operacional em dispositivos móveis a partir de $360\text{px}$ de largura, sem rolagem horizontal ou quebra de layout. |
| **RNF-07** | Manutenibilidade e Arquitetura | O backend deve adotar estritamente o padrão **Vertical Slice by Feature com MVC Convencional**, sem sobrecarga burocrática de Clean Architecture. O frontend deve seguir a arquitetura unilinear de responsabilidades (`Component` $\rightarrow$ `Store` $\rightarrow$ `Composable` $\rightarrow$ `Service`). |
| **RNF-08** | Qualidade e Testabilidade | O núcleo matemático de cálculo e restrições rígidas do backend deve atingir cobertura de testes unitários mínima de 85% em JUnit 5. O frontend deve passar pelo build estrito de TypeScript sem emissão de avisos ou erros. |

---

## 4. Critérios de Aceite em Formato BDD / Gherkin

### Cenário 1: Descarte Imediato por Restrição Rígida de Convivência
```gherkin
Cenário: Adotante com crianças tenta visualizar animal agressivo ou intolerante a crianças
  Dado que o adotante possui crianças de até 6 anos em sua residência
  E o animal cadastrado possui o atributo "sociableWithKids" igual a "false"
  Quando o motor de recomendação avalia a compatibilidade do par
  Então o filtro de restrições rígidas R(u, a) deve resultar em 0 (zero)
  E a pontuação global de compatibilidade Score(u, a) deve ser exatamente 0.0%
  E o animal não deve figurar entre os candidatos elegíveis apresentados ao adotante.
```

### Cenário 2: Cálculo Híbrido Completo com Explicabilidade e Alertas
```gherkin
Cenário: Cálculo de compatibilidade bem-sucedido com análise semântica e explicabilidade
  Dado que o adotante preencheu seu perfil estruturado e relatou: "Trabalho em home office e busco um companheiro calmo"
  E existe um animal cadastrado apto com perfil compatível e diário: "Adora ficar deitado ao lado da mesa durante o dia"
  E as restrições rígidas são plenamente satisfeitas (R(u, a) = 1)
  Quando a requisição de recomendação é processada
  Então o backend deve calcular a similaridade ponderada estruturada
  E o cliente de LLM deve retornar o alinhamento semântico contextual S_LLM(u, a) >= 0.80
  E a resposta deve conter uma lista não vazia de "alignmentFactors" (Fatores de Afinidade)
  E a resposta deve conter uma lista de "preventiveWarnings" alertando sobre a necessidade de passeios regulares
  E a pontuação global exibida no frontend deve refletir a média ponderada convexa calculada.
```

### Cenário 3: Resiliência Operacional com Acionamento de Fallback Heurístico
```gherkin
Cenário: Falha de conectividade ou timeout na API de LLM
  Dado que o adotante solicitou o cálculo de recomendações
  E o provedor externo de LLM ultrapassou o limite de tempo estipulado de 3.500ms
  Quando o backend identifica o timeout na chamada assíncrona
  Então o sistema não deve lançar erro HTTP 500 para o usuário
  E o CHeuristicFallbackClient deve ser ativado imediatamente
  E a pontuação deve ser calculada utilizando similaridade léxica/jaccard e pesos estruturados
  E a resposta deve retornar justificativas padronizadas indicando os pontos fortes e alertas de rotina
  E o frontend deve exibir os resultados normalmente com tempo total de resposta aceitável.
```

### Cenário 4: Prevenção de Requisições Duplicadas (Lock de Interface)
```gherkin
Cenário: Usuário clica repetidamente no botão de calcular recomendação
  Dado que o usuário está no formulário de estilo de vida
  Quando ele aciona o botão "Encontrar Meus Matches"
  Então o botão deve entrar imediatamente em estado de loading
  E todas as interações de reenvio devem ser bloqueadas visualmente e funcionalmente
  E o botão deve permanecer travado até o recebimento da resposta ou timeout da requisição.
```

---

## 5. Matriz de Rastreabilidade Bidirecional (TCC $\leftrightarrow$ Software)

A tabela abaixo evidencia a rastreabilidade entre cada requisito computacional, o referencial teórico do Artigo de TCC e os artefatos de código implementados:

| Requisito | Seção do Artigo de TCC | Referência Científica | Artefatos Backend (Kotlin) | Artefatos Frontend (Vue 3) |
|:---:|:---|:---|:---|:---|
| **RF-01** | Seção 3.1 — Modelagem do Adotante | Hawes et al. (2020) | `features/adopter/domain/CAdopter.kt`<br/>`features/adopter/dto/CAdopterProfileRequest.kt` | `views/QuestionnaireView.vue`<br/>`stores/useAdopterStore.ts` |
| **RF-02** | Seção 3.2 — Caracterização do Animal | Protopopova & Gunter (2017) | `features/pet/domain/CPet.kt`<br/>`features/pet/domain/CPetBehavioralLog.kt` | `views/PetDetailsView.vue`<br/>`stores/usePetStore.ts` |
| **RF-03** | Seção 3.3 — Camada de Restrições Rígidas | Lops et al. (2011) | `features/recommendation/service/CHardConstraintsFilter.kt` | `composables/useRecommendation.ts` |
| **RF-04** | Seção 3.4 — Ponderação Multicritério | Wu et al. (2024) | `features/recommendation/service/CWeightedScorer.kt`<br/>`shared/constants/CWeightConstants.kt` | `components/domain/MatchScoreBadge.vue` |
| **RF-05** | Seção 3.5 — Alinhamento Semântico LLM | Lin et al. (2024) | `features/recommendation/client/CSpringAiLlmClient.kt` | `stores/useRecommendationStore.ts` |
| **RF-06** | Seção 3.6 — Explicabilidade Preventiva | Ribeiro (2026) | `features/recommendation/dto/CRecommendationResponse.kt` | `components/domain/PreventiveWarningCard.vue`<br/>`components/domain/AffinityFactorsList.vue` |
| **RF-07** | Seção 3.7 — Resiliência e Fallback | Lin et al. (2024) | `features/recommendation/client/CHeuristicFallbackClient.kt` | `services/CRecommendationService.ts` |
| **RF-08** | Seção 3.8 — Ranqueamento Global | Lops et al. (2011) | `features/recommendation/service/CRecommendationService.kt` | `views/RecommendationsView.vue` |
| **RF-09** | Seção 3.9 — Interface e Conscientização | Brooke (1996); Davis (1989) | `features/recommendation/controller/CRecommendationController.kt` | `views/RecommendationsView.vue`<br/>`design/adotamatch-design-system.pen` |
| **RF-10** | Seção 4.1 — Fluxo de Adoção Ética | Hawes et al. (2020) | `features/adopter/controller/CAdoptionIntentController.kt` | `components/domain/AdoptionIntentModal.vue` |
| **RF-11** | Seção 4.2 — Gestão e Monitoramento | Protopopova & Gunter (2017) | `features/pet/controller/CPetController.kt` | `views/ShelterDashboardView.vue` |
| **RF-12** | Seção 5.0 — Protocolo de Validação | Powers (2011); Davis (1989) | `features/recommendation/telemetry/CRecommendationLogger.kt` | `composables/useTelemetry.ts` |
