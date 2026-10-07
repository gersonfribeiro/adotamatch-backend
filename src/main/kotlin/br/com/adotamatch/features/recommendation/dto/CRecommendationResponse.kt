package br.com.adotamatch.features.recommendation.dto

import br.com.adotamatch.features.pet.domain.EPetGender
import br.com.adotamatch.features.pet.domain.EPetSize
import br.com.adotamatch.features.pet.domain.ESpecies

/**
 * @description Contrato de resposta do cálculo de compatibilidade com explicabilidade preventiva.
 * @property petId Identificador do animal.
 * @property petName Nome do animal.
 * @property species Espécie (DOG ou CAT).
 * @property gender Sexo biológico do animal.
 * @property size Porte físico.
 * @property ageMonths Idade estimada em meses.
 * @property photoUrl URL da foto de divulgação.
 * @property videoUrl URL de vídeo do animal.
 * @property biography Resumo biográfico do resgate do animal.
 * @property compatibilityScore Pontuação final de compatibilidade normalizada entre 0 e 100.
 * @property semanticScore Escore de alinhamento contextual gerado via LLM (0.0 a 1.0).
 * @property alignmentFactors Fatores positivos que explicam por que o animal combina com o adotante.
 * @property preventiveWarnings Alertas de manejo e desafios reais para prevenir a devolução.
 * @property shelterName Nome da ONG, abrigo ou pet shop mantenedor.
 * @property shelterCity Cidade e UF onde o animal está acolhido.
 * @property contactWhatsapp Número de WhatsApp para contato da adoção.
 */
data class CRecommendationResponse(
    val petId: Long,
    val petName: String,
    val species: ESpecies,
    val gender: EPetGender,
    val size: EPetSize,
    val ageMonths: Int,
    val photoUrl: String,
    val videoUrl: String?,
    val biography: String,
    val compatibilityScore: Double,
    val semanticScore: Double,
    val alignmentFactors: List<String>,
    val preventiveWarnings: List<String>,
    val shelterName: String,
    val shelterCity: String,
    val contactWhatsapp: String
)
