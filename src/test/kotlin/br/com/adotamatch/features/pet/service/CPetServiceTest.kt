package br.com.adotamatch.features.pet.service

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.pet.domain.EPetGender
import br.com.adotamatch.features.pet.domain.EPetSize
import br.com.adotamatch.features.pet.domain.EPetStatus
import br.com.adotamatch.features.pet.domain.ESpecies
import br.com.adotamatch.features.pet.dto.CPetCreateRequest
import br.com.adotamatch.features.pet.repository.IPetRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class CPetServiceTest {

    private lateinit var petRepository: IPetRepository
    private lateinit var petService: CPetService

    @BeforeEach
    fun setup() {
        petRepository = mockk()
        petService = CPetService(petRepository)
    }

    @Test
    fun `deve cadastrar novo animal com sucesso para abrigo parceiro`() {
        val request = CPetCreateRequest(
            name = "Rex",
            species = ESpecies.DOG,
            gender = EPetGender.MALE,
            size = EPetSize.MEDIUM,
            ageMonths = 18,
            energyLevel = 3,
            solitaryToleranceHours = 6,
            kidsFriendly = true,
            petsFriendly = true,
            photoUrl = "https://images.unsplash.com/photo-rex",
            biography = "Resgatado dócil.",
            behavioralNotes = "Calmo e companheiro.",
            shelterName = "Pet Shop Parceiro",
            shelterCity = "Ubá - MG",
            contactWhatsapp = "32999999999"
        )

        val savedPet = CPet(
            id = 10L,
            name = "Rex",
            species = ESpecies.DOG,
            gender = EPetGender.MALE,
            size = EPetSize.MEDIUM,
            ageMonths = 18,
            status = EPetStatus.AVAILABLE,
            energyLevel = 3,
            solitaryToleranceHours = 6,
            kidsFriendly = true,
            petsFriendly = true,
            photoUrl = "https://images.unsplash.com/photo-rex",
            biography = "Resgatado dócil.",
            behavioralNotes = "Calmo e companheiro.",
            shelterName = "Pet Shop Parceiro",
            shelterCity = "Ubá - MG",
            contactWhatsapp = "32999999999"
        )

        every { petRepository.save(any()) } returns savedPet

        val response = petService.create(request)

        assertNotNull(response)
        assertEquals(10L, response.id)
        assertEquals("Rex", response.name)
        assertEquals("Pet Shop Parceiro", response.shelterName)
        verify(exactly = 1) { petRepository.save(any()) }
    }

    @Test
    fun `deve filtrar animais por especie e status`() {
        val petDog = CPet(
            id = 1L,
            name = "Bidu",
            species = ESpecies.DOG,
            status = EPetStatus.AVAILABLE
        )
        val petCat = CPet(
            id = 2L,
            name = "Mingau",
            species = ESpecies.CAT,
            status = EPetStatus.AVAILABLE
        )

        every { petRepository.findAll() } returns listOf(petDog, petCat)

        val dogs = petService.findAll(pSpecies = ESpecies.DOG, pStatus = EPetStatus.AVAILABLE, pSearch = null)

        assertEquals(1, dogs.size)
        assertEquals("Bidu", dogs[0].name)
    }
}
