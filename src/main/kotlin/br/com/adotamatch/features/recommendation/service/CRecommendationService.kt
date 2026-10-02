package br.com.adotamatch.features.recommendation.service

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.pet.domain.EPetSize
import br.com.adotamatch.features.pet.domain.EPetStatus
import br.com.adotamatch.features.pet.domain.ESpecies
import br.com.adotamatch.features.pet.repository.IPetRepository
import br.com.adotamatch.features.recommendation.client.ILlmClient
import br.com.adotamatch.features.recommendation.dto.CRecommendationRequest
import br.com.adotamatch.features.recommendation.dto.CRecommendationResponse
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @description Serviço central do motor de recomendação inteligente do AdotaMatch.
 * Orquestra as restrições rígidas, o alinhamento via LLM e o ranqueamento multicritério.
 */
@Service
class CRecommendationService(
    private val petRepository: IPetRepository,
    private val hardConstraintsFilter: CHardConstraintsFilter,
    private val weightedScorer: CWeightedScorer,
    private val llmClient: ILlmClient
) {

    /**
     * @description Popula o banco em memória com registros iniciais para testes imediatos da banca.
     */
    @PostConstruct
    fun initSampleData() {
        if (petRepository.count() == 0L) {
            petRepository.saveAll(
                listOf(
                    CPet(
                        name = "Caramelo",
                        species = ESpecies.DOG,
                        size = EPetSize.MEDIUM,
                        status = EPetStatus.AVAILABLE,
                        energyLevel = 3,
                        solitaryToleranceHours = 6,
                        kidsFriendly = true,
                        petsFriendly = true,
                        requiresSpecialCare = false,
                        behavioralNotes = "Extremamente dócil e equilibrado. Convive com crianças e gosta de caminhadas tranquilas no final da tarde.",
                        shelterName = "ONG Amor de Patas",
                        shelterCity = "Ubá"
                    ),
                    CPet(
                        name = "Thor",
                        species = ESpecies.DOG,
                        size = EPetSize.LARGE,
                        status = EPetStatus.AVAILABLE,
                        energyLevel = 5,
                        solitaryToleranceHours = 4,
                        kidsFriendly = false,
                        petsFriendly = false,
                        requiresSpecialCare = false,
                        behavioralNotes = "Jovem hiperativo com muita força física. Necessita de tutor experiente, espaço amplo e rotina intensa de exercícios.",
                        shelterName = "SOS Animais",
                        shelterCity = "Visconde do Rio Branco"
                    ),
                    CPet(
                        name = "Mia",
                        species = ESpecies.CAT,
                        size = EPetSize.SMALL,
                        status = EPetStatus.AVAILABLE,
                        energyLevel = 2,
                        solitaryToleranceHours = 8,
                        kidsFriendly = true,
                        petsFriendly = true,
                        requiresSpecialCare = false,
                        behavioralNotes = "Gata tranquila, ronrona com facilidade e tolera bem períodos sozinha durante o dia.",
                        shelterName = "Associação Bichos & Cia",
                        shelterCity = "Ubá"
                    ),
                    CPet(
                        name = "Pipoca",
                        species = ESpecies.DOG,
                        size = EPetSize.SMALL,
                        status = EPetStatus.AVAILABLE,
                        energyLevel = 3,
                        solitaryToleranceHours = 6,
                        kidsFriendly = true,
                        petsFriendly = true,
                        requiresSpecialCare = false,
                        behavioralNotes = "Ideal para apartamentos. Muito brincalhão e amoroso com a família.",
                        shelterName = "ONG Amor de Patas",
                        shelterCity = "Ubá"
                    )
                )
            )
        }
    }

    /**
     * @description Executa a compatibilização ética de candidatos, ranqueando os animais por afinidade e explicabilidade.
     * @param pRequest Dados estruturados e relato de estilo de vida do adotante.
     * @returns Lista ordenada de recomendações com escore e alertas preventivos.
     */
    @Transactional(readOnly = true)
    fun findMatches(pRequest: CRecommendationRequest): List<CRecommendationResponse> {
        val availablePets = petRepository.findByStatus(EPetStatus.AVAILABLE)

        return availablePets
            // 1. Camada de Restrições Rígidas R(u, a)
            .filter { pPet -> hardConstraintsFilter.evaluate(pRequest, pPet) }
            // 2. Processamento Híbrido (Ponderação + LLM)
            .map { pPet ->
                val llmResult = llmClient.evaluate(pRequest, pPet)
                val finalScore = weightedScorer.calculateScore(
                    pRequest = pRequest,
                    pPet = pPet,
                    pSemanticScore = llmResult.semanticScore
                )

                CRecommendationResponse(
                    petId = pPet.id ?: 0L,
                    petName = pPet.name,
                    species = pPet.species,
                    size = pPet.size,
                    compatibilityScore = finalScore,
                    semanticScore = llmResult.semanticScore,
                    alignmentFactors = llmResult.alignmentFactors,
                    preventiveWarnings = llmResult.preventiveWarnings,
                    shelterName = pPet.shelterName,
                    shelterCity = pPet.shelterCity
                )
            }
            // 3. Ordenação decrescente por pontuação de compatibilidade
            .sortedByDescending { it.compatibilityScore }
    }
}
