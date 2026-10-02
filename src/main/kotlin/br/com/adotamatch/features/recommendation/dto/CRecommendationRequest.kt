package br.com.adotamatch.features.recommendation.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * @description Tipos de moradia suportados.
 */
enum class EAdopterHousing {
    APARTMENT,
    HOUSE_NO_YARD,
    HOUSE_YARD,
    RURAL
}

/**
 * @description Preferência de espécie para a recomendação.
 */
enum class ESpeciesPreference {
    DOG,
    CAT,
    ANY
}

/**
 * @description Payload de entrada para requisição do motor de recomendação inteligente.
 * @property speciesPreference Preferência declarada de espécie.
 * @property housingType Tipo de moradia do adotante.
 * @property dailyHoursAvailable Horas diárias disponíveis para interação e cuidados.
 * @property hasChildren Indica se residem crianças na residência.
 * @property hasOtherPets Indica se já existem outros animais convivendo no lar.
 * @property experienceLevel Grau de experiência prévia com animais (1 a 3).
 * @property lifestyleNarrative Relato em texto livre sobre rotina diária e expectativas.
 */
data class CRecommendationRequest(
    @field:NotNull(message = "A preferência de espécie é obrigatória.")
    val speciesPreference: ESpeciesPreference,

    @field:NotNull(message = "O tipo de moradia é obrigatório.")
    val housingType: EAdopterHousing,

    @field:Min(value = 1, message = "O tempo diário disponível deve ser de no mínimo 1 hora.")
    @field:Max(value = 16, message = "O tempo diário disponível não pode exceder 16 horas.")
    val dailyHoursAvailable: Int,

    val hasChildren: Boolean = false,

    val hasOtherPets: Boolean = false,

    @field:Min(value = 1, message = "O nível de experiência mínimo é 1.")
    @field:Max(value = 3, message = "O nível de experiência máximo é 3.")
    val experienceLevel: Int = 2,

    @field:NotBlank(message = "O relato de rotina e estilo de vida é obrigatório para processamento pela LLM.")
    val lifestyleNarrative: String
)
