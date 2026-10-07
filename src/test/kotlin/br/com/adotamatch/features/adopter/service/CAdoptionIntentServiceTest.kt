package br.com.adotamatch.features.adopter.service

import br.com.adotamatch.features.adopter.domain.CAdoptionIntent
import br.com.adotamatch.features.adopter.domain.EAdoptionStatus
import br.com.adotamatch.features.adopter.dto.CAdoptionIntentRequest
import br.com.adotamatch.features.adopter.repository.IAdoptionIntentRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CAdoptionIntentServiceTest {

    private lateinit var intentRepository: IAdoptionIntentRepository
    private lateinit var intentService: CAdoptionIntentService

    @BeforeEach
    fun setup() {
        intentRepository = mockk()
        intentService = CAdoptionIntentService(intentRepository)
    }

    @Test
    fun `deve registrar intencao de adocao com sucesso`() {
        val request = CAdoptionIntentRequest(
            petId = 1L,
            petName = "Caramelo",
            adopterName = "Carlos Silva",
            adopterEmail = "carlos@email.com",
            adopterPhone = "32988887777",
            adopterCity = "Ubá - MG",
            compatibilityScore = 88.5,
            adopterNarrative = "Rotina calma e casa com quintal.",
            messageToShelter = "Gostaria muito de adotar o Caramelo."
        )

        val savedIntent = CAdoptionIntent(
            id = 100L,
            petId = 1L,
            petName = "Caramelo",
            adopterName = "Carlos Silva",
            adopterEmail = "carlos@email.com",
            adopterPhone = "32988887777",
            adopterCity = "Ubá - MG",
            compatibilityScore = 88.5,
            adopterNarrative = "Rotina calma e casa com quintal.",
            messageToShelter = "Gostaria muito de adotar o Caramelo.",
            status = EAdoptionStatus.SUBMITTED,
            createdAt = LocalDateTime.now()
        )

        every { intentRepository.save(any()) } returns savedIntent

        val response = intentService.submitIntent(request)

        assertNotNull(response)
        assertEquals(100L, response.id)
        assertEquals("Carlos Silva", response.adopterName)
        assertEquals(88.5, response.compatibilityScore)
        verify(exactly = 1) { intentRepository.save(any()) }
    }
}
