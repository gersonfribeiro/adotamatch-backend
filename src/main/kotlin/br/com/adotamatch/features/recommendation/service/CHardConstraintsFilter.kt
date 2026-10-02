package br.com.adotamatch.features.recommendation.service

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.pet.domain.EPetSize
import br.com.adotamatch.features.pet.domain.EPetStatus
import br.com.adotamatch.features.pet.domain.ESpecies
import br.com.adotamatch.features.recommendation.dto.CRecommendationRequest
import br.com.adotamatch.features.recommendation.dto.EAdopterHousing
import br.com.adotamatch.features.recommendation.dto.ESpeciesPreference
import org.springframework.stereotype.Component

/**
 * @description Camada de restrições rígidas eliminatórias R(u, a) do modelo AdotaMatch.
 * Descarta de imediato alternativas com incompatibilidades críticas intransponíveis.
 */
@Component
class CHardConstraintsFilter {

    /**
     * @description Avalia se o par adotante-animal satisfaz todas as condições obrigatórias.
     * @param pRequest Dados do perfil do adotante.
     * @param pPet Dados do animal candidato.
     * @returns True se o animal for elegível para o cálculo ponderado; False para descarte imediato.
     */
    fun evaluate(pRequest: CRecommendationRequest, pPet: CPet): Boolean {
        // 1. O animal deve estar disponível para adoção
        if (pPet.status != EPetStatus.AVAILABLE) {
            return false
        }

        // 2. Preferência de espécie estrita
        if (pRequest.speciesPreference == ESpeciesPreference.DOG && pPet.species != ESpecies.DOG) {
            return false
        }
        if (pRequest.speciesPreference == ESpeciesPreference.CAT && pPet.species != ESpecies.CAT) {
            return false
        }

        // 3. Incompatibilidade habitacional crítica (animal grande em apartamento sem espaço)
        if (pRequest.housingType == EAdopterHousing.APARTMENT && pPet.size == EPetSize.LARGE && pPet.energyLevel >= 4) {
            return false
        }

        // 4. Incompatibilidade com crianças caso o animal tenha intolerância comprovada
        if (pRequest.hasChildren && !pPet.kidsFriendly) {
            return false
        }

        return true
    }
}
