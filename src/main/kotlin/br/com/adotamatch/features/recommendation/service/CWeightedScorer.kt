package br.com.adotamatch.features.recommendation.service

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.pet.domain.EPetSize
import br.com.adotamatch.features.recommendation.dto.CRecommendationRequest
import br.com.adotamatch.features.recommendation.dto.EAdopterHousing
import org.springframework.stereotype.Component
import kotlin.math.abs
import kotlin.math.max

/**
 * @description Camada de cálculo da similaridade ponderada multicritério.
 * Implementa formalmente as funções de similaridade s_c(u, a) descritas na monografia acadêmica.
 */
@Component
class CWeightedScorer {

    companion object {
        const val WEIGHT_ENERGY_TIME = 0.20
        const val WEIGHT_KIDS_PETS = 0.15
        const val WEIGHT_HOUSING_SIZE = 0.15
        const val WEIGHT_SOLITARY_ROUTINE = 0.15
        const val WEIGHT_EXPERIENCE_CARE = 0.15
        const val WEIGHT_LLM_SEMANTIC = 0.20
    }

    /**
     * @description Calcula a pontuação final de compatibilidade combinando os critérios estruturados e o escore LLM.
     * @param pRequest Dados do perfil do adotante.
     * @param pPet Dados do animal sob custódia.
     * @param pSemanticScore Escore semântico contextual apurado via LLM (0.0 a 1.0).
     * @returns Pontuação final normalizada entre 0.0 e 100.0.
     */
    fun calculateScore(
        pRequest: CRecommendationRequest,
        pPet: CPet,
        pSemanticScore: Double
    ): Double {
        val s1 = calculateEnergyTimeSimilarity(pPet.energyLevel, pRequest.dailyHoursAvailable)
        val s2 = calculateKidsPetsSimilarity(pRequest.hasChildren, pRequest.hasOtherPets, pPet.kidsFriendly, pPet.petsFriendly)
        val s3 = calculateHousingSizeSimilarity(pRequest.housingType, pPet.size)
        val s4 = calculateSolitarySimilarity(pPet.solitaryToleranceHours, pRequest.dailyHoursAvailable)
        val s5 = calculateExperienceCareSimilarity(pRequest.experienceLevel, pPet.requiresSpecialCare)

        val weightedSum = (WEIGHT_ENERGY_TIME * s1) +
                (WEIGHT_KIDS_PETS * s2) +
                (WEIGHT_HOUSING_SIZE * s3) +
                (WEIGHT_SOLITARY_ROUTINE * s4) +
                (WEIGHT_EXPERIENCE_CARE * s5) +
                (WEIGHT_LLM_SEMANTIC * pSemanticScore.coerceIn(0.0, 1.0))

        return (weightedSum * 100.0).coerceIn(0.0, 100.0)
    }

    /**
     * @description Similaridade s1: Energia do animal vs tempo diário disponível.
     */
    private fun calculateEnergyTimeSimilarity(pEnergyLevel: Int, pDailyHours: Int): Double {
        val normalizedHours = when {
            pDailyHours <= 2 -> 1
            pDailyHours in 3..4 -> 2
            pDailyHours in 5..6 -> 3
            pDailyHours in 7..8 -> 4
            else -> 5
        }
        val diff = abs(pEnergyLevel - normalizedHours)
        return (1.0 - (diff / 4.0)).coerceIn(0.0, 1.0)
    }

    /**
     * @description Similaridade s2: Convivência familiar (crianças e outros animais).
     */
    private fun calculateKidsPetsSimilarity(
        pHasKids: Boolean,
        pHasPets: Boolean,
        pKidsFriendly: Boolean,
        pPetsFriendly: Boolean
    ): Double {
        var score = 1.0
        if (pHasKids && !pKidsFriendly) score -= 0.5
        if (pHasPets && !pPetsFriendly) score -= 0.5
        return score.coerceIn(0.0, 1.0)
    }

    /**
     * @description Similaridade s3: Espaço físico de moradia vs porte do animal.
     */
    private fun calculateHousingSizeSimilarity(pHousing: EAdopterHousing, pSize: EPetSize): Double {
        val housingScale = when (pHousing) {
            EAdopterHousing.APARTMENT -> 1
            EAdopterHousing.HOUSE_NO_YARD -> 2
            EAdopterHousing.HOUSE_YARD -> 3
            EAdopterHousing.RURAL -> 4
        }
        val sizeScale = when (pSize) {
            EPetSize.MINI -> 1
            EPetSize.SMALL -> 2
            EPetSize.MEDIUM -> 3
            EPetSize.LARGE -> 4
        }
        val diff = abs(housingScale - sizeScale)
        return (1.0 - (diff / 3.0)).coerceIn(0.0, 1.0)
    }

    /**
     * @description Similaridade s4: Tolerância à solidão vs rotina diária.
     */
    private fun calculateSolitarySimilarity(pSolitaryToleranceHours: Int, pDailyHours: Int): Double {
        // Estima ausência como (16 horas ativas - horas disponíveis)
        val estimatedAbsence = max(0, 12 - pDailyHours)
        return if (estimatedAbsence <= pSolitaryToleranceHours) {
            1.0
        } else {
            max(0.0, 1.0 - ((estimatedAbsence - pSolitaryToleranceHours) / 4.0))
        }
    }

    /**
     * @description Similaridade s5: Experiência prévia vs cuidados especiais.
     */
    private fun calculateExperienceCareSimilarity(pExpLevel: Int, pRequiresCare: Boolean): Double {
        val careDemand = if (pRequiresCare) 3 else 1
        return if (pExpLevel >= careDemand) {
            1.0
        } else {
            max(0.0, 1.0 - ((careDemand - pExpLevel) * 0.4))
        }
    }
}
