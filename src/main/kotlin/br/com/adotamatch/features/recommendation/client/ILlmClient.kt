package br.com.adotamatch.features.recommendation.client

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.recommendation.dto.CRecommendationRequest

/**
 * @description Contrato do cliente de inferência em Modelos de Linguagem de Grande Porte.
 */
interface ILlmClient {

    /**
     * @description Avalia a compatibilidade contextual entre a narrativa do adotante e o diário do animal.
     * @param pRequest Dados e narrativa de rotina do adotante.
     * @param pPet Dados descritivos e comportamentais do animal.
     * @returns Avaliação estruturada com score semântico e justificativas em linguagem natural.
     */
    fun evaluate(pRequest: CRecommendationRequest, pPet: CPet): CLlmEvaluationResult
}
