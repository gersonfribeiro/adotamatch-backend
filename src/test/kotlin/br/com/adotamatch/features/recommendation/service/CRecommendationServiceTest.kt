package br.com.adotamatch.features.recommendation.service

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.pet.domain.EPetSize
import br.com.adotamatch.features.pet.domain.EPetStatus
import br.com.adotamatch.features.pet.domain.ESpecies
import br.com.adotamatch.features.pet.repository.IPetRepository
import br.com.adotamatch.features.recommendation.client.CHeuristicFallbackClient
import br.com.adotamatch.features.recommendation.dto.CRecommendationRequest
import br.com.adotamatch.features.recommendation.dto.EAdopterHousing
import br.com.adotamatch.features.recommendation.dto.ESpeciesPreference
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CRecommendationServiceTest {

    private lateinit var petRepository: IPetRepository
    private lateinit var hardConstraintsFilter: CHardConstraintsFilter
    private lateinit var weightedScorer: CWeightedScorer
    private lateinit var llmClient: CHeuristicFallbackClient
    private lateinit var recommendationService: CRecommendationService

    @BeforeEach
    fun setup() {
        petRepository = mockk()
        hardConstraintsFilter = CHardConstraintsFilter()
        weightedScorer = CWeightedScorer()
        llmClient = CHeuristicFallbackClient()
        recommendationService = CRecommendationService(
            petRepository,
            hardConstraintsFilter,
            weightedScorer,
            llmClient
        )
    }

    @Test
    fun `deve ranquear animais compativeis e descartar especies divergentes da preferencia`() {
        val petDog = CPet(
            id = 1L,
            name = "Caramelo",
            species = ESpecies.DOG,
            size = EPetSize.MEDIUM,
            status = EPetStatus.AVAILABLE,
            energyLevel = 3,
            solitaryToleranceHours = 6,
            kidsFriendly = true,
            petsFriendly = true
        )

        val petCat = CPet(
            id = 2L,
            name = "Mia",
            species = ESpecies.CAT,
            size = EPetSize.SMALL,
            status = EPetStatus.AVAILABLE,
            energyLevel = 2,
            solitaryToleranceHours = 8,
            kidsFriendly = true,
            petsFriendly = true
        )

        every { petRepository.findByStatus(EPetStatus.AVAILABLE) } returns listOf(petDog, petCat)

        val request = CRecommendationRequest(
            speciesPreference = ESpeciesPreference.DOG,
            housingType = EAdopterHousing.HOUSE_YARD,
            dailyHoursAvailable = 4,
            hasChildren = true,
            hasOtherPets = false,
            experienceLevel = 2,
            lifestyleNarrative = "Trabalho em regime de home office três dias por semana e busco um cão dócil para caminhadas."
        )

        val results = recommendationService.findMatches(request)

        // Deve retornar apenas o cão, pois a preferência é DOG
        assertEquals(1, results.size)
        assertEquals("Caramelo", results[0].petName)
        assertTrue(results[0].compatibilityScore > 70.0, "O score deve ser superior a 70%")
        assertTrue(results[0].alignmentFactors.isNotEmpty(), "Deve conter fatores de alinhamento")
        assertTrue(results[0].preventiveWarnings.isNotEmpty(), "Deve conter alertas preventivos")
    }

    @Test
    fun `deve descartar animal indisponivel pela camada de restricoes rigidas`() {
        val adoptedPet = CPet(
            id = 3L,
            name = "Rex",
            species = ESpecies.DOG,
            status = EPetStatus.ADOPTED
        )

        val request = CRecommendationRequest(
            speciesPreference = ESpeciesPreference.ANY,
            housingType = EAdopterHousing.HOUSE_YARD,
            dailyHoursAvailable = 4,
            lifestyleNarrative = "Rotina comum."
        )

        val isEligible = hardConstraintsFilter.evaluate(request, adoptedPet)
        assertFalse(isEligible, "Animais já adotados devem ser imediatamente descartados")
    }
}
