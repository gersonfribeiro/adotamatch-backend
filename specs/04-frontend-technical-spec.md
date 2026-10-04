# Especificação 04 — Especificação Técnica do Frontend
## Stack: Vue 3 + Vite + PrimeVue + Tailwind CSS | TLC Spec-Driven Development

---

## 1. Visão Geral do Módulo Frontend

O frontend do AdotaMatch é uma SPA moderna orientada a componentes, estilizada através da integração harmônica entre **PrimeVue** (para componentes ricos como Cards, Diálogos, Inputs, Steppers e Toasts) e **Tailwind CSS** (para layout utilitário, espaçamentos, tipografia e responsividade).

Segue integralmente a [Constituição do Frontend](file:///c:/Users/gerso/OneDrive/Documentos/TCC/adotamatch/frontend/constitution.md) e as convenções globais do projeto.

---

## 2. Mapa de Rotas e Navegação (`src/router/index.ts`)

| Caminho da Rota | Nome | View / Componente | Descrição |
| :--- | :--- | :--- | :--- |
| `/` | `home` | `HomeView.vue` | Apresentação da proposta de adoção responsável, dados de impacto e chamada para ação. |
| `/match` | `adopter-match` | `AdopterMatchView.vue` | Formulário interativo em etapas (*Stepper*) para preenchimento de variáveis e narrativa do adotante. |
| `/recommendations`| `recommendations` | `RecommendationsView.vue` | Exibição ranqueada dos animais compatíveis com expansão de justificativas e alertas preventivos. |
| `/pets` | `pet-catalog` | `PetCatalogView.vue` | Catálogo geral de animais disponíveis sob custódia das ONGs parceiras. |
| `/shelter/pets/new`| `pet-register`| `PetRegisterView.vue` | Cadastro veterinário e comportamental de novo animal (área restrita de abrigos/protetores). |

---

## 3. Arquitetura de Stores Pinia (`src/stores/`)

### 3.1 `useRecommendationStore.ts`
* **Estado Reativo (`state`):**
  * `adopterProfile`: Objeto `IAdopterProfileRequest` preenchido no formulário.
  * `recommendations`: Lista ordenada de `IRecommendationResponse`.
  * `isLoading`: Booleano para lock visual de processamento da recomendação.
  * `hasError`: Booleano indicando falha na requisição.
* **Ações (`actions`):**
  * `generateMatches(pProfile: IAdopterProfileRequest): Promise<void>`: Aciona o serviço `CRecommendationService.match()`, ativa o lock de requisição, manipula erros com Toast e redireciona para a tela `/recommendations`.

### 3.2 `usePetStore.ts`
* **Estado Reativo (`state`):**
  * `pets`: Lista de `IPetSummaryResponse`.
  * `selectedPet`: Objeto `IPetDetailsResponse | null`.
  * `isLoadingPets`: Booleano para skeleton loaders.

---

## 4. Componentização e Design System (PrimeVue + Tailwind)

### 4.1 Componentes de Domínio (`src/components/domain/`)
1. **`MatchCard.vue`:**
   * Renderiza a foto do animal, nome, porte, idade e o selo visual de compatibilidade global (`% Score`).
   * Exibe botão de expansão com transição suave para detalhes explicativos.
2. **`ExplanationBanner.vue`:**
   * Renderiza os **Fatores de Afinidade** com ícones positivos (Check) em card suave com bordas verdes (`border-emerald-200 bg-emerald-50/50`).
   * Renderiza os **Alertas Preventivos de Adaptação** com ícones de atenção (AlertTriangle) em card âmbar (`border-amber-200 bg-amber-50/50`), instruindo o adotante sobre os cuidados necessários para evitar a devolução.
3. **`AdopterWizard.vue`:**
   * Formulário em múltiplos passos (*PrimeVue Stepper*):
     * Passo 1: Informações de Habitação e Espaço Físico.
     * Passo 2: Rotina e Disponibilidade de Horários.
     * Passo 3: Composição Familiar e Experiência com Animais.
     * Passo 4: Relato Livre de Estilo de Vida (Textarea com contador de caracteres e dicas de preenchimento).

---

## 5. Exemplo Canônico de Componente com Lock de Requisição

```vue
<script setup lang="ts">
// Types e Interfaces
/**
 * @property {boolean} loading - Controla o estado de processamento visual.
 */
type TProps = {
  loading: boolean;
};

/**
 * @property {[]} submit - Disparado quando o adotante conclui o preenchimento.
 */
type TEmits = {
  submit: [];
};

// Props
const props = defineProps<TProps>();

// Emits
const emit = defineEmits<TEmits>();

// Reativas - Ref
const isLocked = ref(false);

// Computadas
const canSubmit = computed(() => !props.loading && !isLocked.value);

// Funções
async function handleSubmit(): Promise<void> {
  if (!canSubmit.value) return;
  isLocked.value = true;
  try {
    emit('submit');
  } finally {
    // Garante liberação do lock após execução
    isLocked.value = false;
  }
}
</script>

<template>
  <div class="flex justify-end p-4">
    <Button
      label="Descobrir Animais Compatíveis"
      icon="pi pi-sparkles"
      :loading="props.loading || isLocked"
      :disabled="!canSubmit"
      class="bg-indigo-600 hover:bg-indigo-700 text-white font-medium px-6 py-3 rounded-xl transition duration-200"
      @click="handleSubmit"
    />
  </div>
</template>
```
