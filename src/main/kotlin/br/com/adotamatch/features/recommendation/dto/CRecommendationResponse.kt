package br.com.adotamatch.features.recommendation.dto

import br.com.adotamatch.features.pet.domain.EPetSize
import br.com.adotamatch.features.pet.domain.ESpecies

/**
 * @description Contrato de resposta do cálculo de compatibilidade com explicabilidade preventiva.
 * @property petId Identificador do animal.
 * @property petName Nome do animal.
 * @property species Espécie (DOG ou CAT).
 * @property size Porte físico.
 * @property compatibilityScore Pontuação final de compatibilidade normalizada entre 0 e 100.
 * @property semanticScore Escore de alinhamento contextual gerado via LLM (0.0 a 1.0).
 * @property alignmentFactors Fatores positivos que explicam por que o animal combina com o adotante.
 * @property preventiveWarnings Alertas de manejo e desafios reais para prevenir a devolução.
 * @property shelterName Nome da ONG ou abrigo responsável.
 * @property shelterCity Cidade onde o animal está acolhido.
 */
data class CRecommendationResponse(
    val petId: Long,
    val petName: String,
    val species: ESpecies,
    val size: EPetSize,
    val compatibilityScore: Double,
    val semanticScore: Double,
    val alignmentFactors: List<String>,
    val preventiveWarnings: List<String>,
    val shelterName: String,
    val shelterCity: String
)
