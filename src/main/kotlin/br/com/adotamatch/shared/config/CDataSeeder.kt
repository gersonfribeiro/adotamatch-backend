package br.com.adotamatch.shared.config

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.pet.domain.EPetGender
import br.com.adotamatch.features.pet.domain.EPetSize
import br.com.adotamatch.features.pet.domain.EPetStatus
import br.com.adotamatch.features.pet.domain.ESpecies
import br.com.adotamatch.features.pet.repository.IPetRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @description Classe de configuração para semear dados representativos de animais acolhidos por ONGs e Pet Shops parceiros.
 */
@Configuration
class CDataSeeder {

    /**
     * @description Executa a carga inicial de animais diversificados para testes da banca e do usuário no MVP.
     * @param pPetRepository Repositório JPA de animais.
     * @returns Instância de CommandLineRunner gerenciada pelo Spring Boot.
     */
    @Bean
    fun seedSamplePets(pPetRepository: IPetRepository): CommandLineRunner {
        return CommandLineRunner {
            if (pPetRepository.count() == 0L) {
                pPetRepository.saveAll(
                    listOf(
                        CPet(
                            name = "Caramelo",
                            species = ESpecies.DOG,
                            gender = EPetGender.MALE,
                            size = EPetSize.MEDIUM,
                            ageMonths = 24,
                            status = EPetStatus.AVAILABLE,
                            energyLevel = 3,
                            solitaryToleranceHours = 6,
                            kidsFriendly = true,
                            petsFriendly = true,
                            requiresSpecialCare = false,
                            isVaccinated = true,
                            isNeutered = true,
                            photoUrl = "https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=800&auto=format&fit=crop&q=80",
                            additionalPhotos = "https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=800&auto=format&fit=crop&q=80",
                            videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
                            biography = "Caramelo foi resgatado em uma praça pública no centro de Ubá. É o típico cão companheiro, leal e extremamente dócil com todos.",
                            behavioralNotes = "Extremamente equilibrado. Gosta de caminhar no final da tarde, não late por qualquer barulho e convive harmoniosamente com crianças e gatos.",
                            shelterName = "ONG Amor de Patas",
                            shelterCity = "Ubá - MG",
                            contactWhatsapp = "32999887766"
                        ),
                        CPet(
                            name = "Luna",
                            species = ESpecies.DOG,
                            gender = EPetGender.FEMALE,
                            size = EPetSize.SMALL,
                            ageMonths = 14,
                            status = EPetStatus.AVAILABLE,
                            energyLevel = 2,
                            solitaryToleranceHours = 7,
                            kidsFriendly = true,
                            petsFriendly = true,
                            requiresSpecialCare = false,
                            isVaccinated = true,
                            isNeutered = true,
                            photoUrl = "https://images.unsplash.com/photo-1537151608828-ea2b11777ee8?w=800&auto=format&fit=crop&q=80",
                            additionalPhotos = "https://images.unsplash.com/photo-1591769225440-811ad7d6eab2?w=800&auto=format&fit=crop&q=80",
                            videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
                            biography = "Luna foi acolhida após abandono em uma caixa de papelão. É carinhosa, porte pequeno e adora colo.",
                            behavioralNotes = "Excelente para apartamentos. Muito tranquila durante o expediente de home office, adora cochilar aos pés do tutor.",
                            shelterName = "Pet Shop & Resgate Mundo Animal",
                            shelterCity = "Ubá - MG",
                            contactWhatsapp = "32998765432"
                        ),
                        CPet(
                            name = "Thor",
                            species = ESpecies.DOG,
                            gender = EPetGender.MALE,
                            size = EPetSize.LARGE,
                            ageMonths = 18,
                            status = EPetStatus.AVAILABLE,
                            energyLevel = 5,
                            solitaryToleranceHours = 4,
                            kidsFriendly = false,
                            petsFriendly = false,
                            requiresSpecialCare = false,
                            isVaccinated = true,
                            isNeutered = true,
                            photoUrl = "https://images.unsplash.com/photo-1552053831-71594a27632d?w=800&auto=format&fit=crop&q=80",
                            additionalPhotos = "https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=800&auto=format&fit=crop&q=80",
                            videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
                            biography = "Thor é um mestiço de labrador com muita vitalidade, resgatado após ser mantido acorrentado em lote vago.",
                            behavioralNotes = "Jovem hiperativo com muita força física. Requer tutor com experiência prévia, casa com quintal amplo e exercícios vigorosos diários. Não recomendado para casas com crianças pequenas.",
                            shelterName = "SOS Animais & Cia",
                            shelterCity = "Visconde do Rio Branco - MG",
                            contactWhatsapp = "32991234567"
                        ),
                        CPet(
                            name = "Mia",
                            species = ESpecies.CAT,
                            gender = EPetGender.FEMALE,
                            size = EPetSize.SMALL,
                            ageMonths = 10,
                            status = EPetStatus.AVAILABLE,
                            energyLevel = 2,
                            solitaryToleranceHours = 9,
                            kidsFriendly = true,
                            petsFriendly = true,
                            requiresSpecialCare = false,
                            isVaccinated = true,
                            isNeutered = true,
                            photoUrl = "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=800&auto=format&fit=crop&q=80",
                            additionalPhotos = "https://images.unsplash.com/photo-1573865526739-10659fec78a5?w=800&auto=format&fit=crop&q=80",
                            videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
                            biography = "Mia foi resgatada ainda filhote no motor de um caminhão. É dócil, ronrona ao menor carinho e gosta de brinquedos com penas.",
                            behavioralNotes = "Gata dócil e independente. Lida bem com períodos em que a família está fora trabalhando, adaptada a apartamento telado.",
                            shelterName = "Associação Bichos & Cia",
                            shelterCity = "Ubá - MG",
                            contactWhatsapp = "32997654321"
                        ),
                        CPet(
                            name = "Frajola",
                            species = ESpecies.CAT,
                            gender = EPetGender.MALE,
                            size = EPetSize.MEDIUM,
                            ageMonths = 36,
                            status = EPetStatus.AVAILABLE,
                            energyLevel = 3,
                            solitaryToleranceHours = 8,
                            kidsFriendly = true,
                            petsFriendly = true,
                            requiresSpecialCare = false,
                            isVaccinated = true,
                            isNeutered = true,
                            photoUrl = "https://images.unsplash.com/photo-1533738363-b7f9aef128ce?w=800&auto=format&fit=crop&q=80",
                            additionalPhotos = "https://images.unsplash.com/photo-1561948955-570b270e7c36?w=800&auto=format&fit=crop&q=80",
                            videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
                            biography = "Frajola é um gato preto e branco com olhar expressivo. Muito sociável, adora observar a rua pela janela telada.",
                            behavioralNotes = "Sociável e pacífico. Convive bem com cães calmos e não se assusta facilmente com barulhos da casa.",
                            shelterName = "Pet Shop & Clínica Veterinária Bem Estar",
                            shelterCity = "Ubá - MG",
                            contactWhatsapp = "32996543210"
                        ),
                        CPet(
                            name = "Pipoca",
                            species = ESpecies.DOG,
                            gender = EPetGender.MALE,
                            size = EPetSize.SMALL,
                            ageMonths = 8,
                            status = EPetStatus.AVAILABLE,
                            energyLevel = 4,
                            solitaryToleranceHours = 5,
                            kidsFriendly = true,
                            petsFriendly = true,
                            requiresSpecialCare = false,
                            isVaccinated = true,
                            isNeutered = false,
                            photoUrl = "https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=800&auto=format&fit=crop&q=80",
                            additionalPhotos = "https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=800&auto=format&fit=crop&q=80",
                            videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
                            biography = "Pipoca é um filhote alegre e cheio de curiosidade. Resgatado com a mãe e irmãos perto da rodoviária.",
                            behavioralNotes = "Filhote carinhoso e receptivo a comandos básicos. Precisa de tutor disposto a ensinar hábitos de higiene e passeios diários.",
                            shelterName = "ONG Amor de Patas",
                            shelterCity = "Ubá - MG",
                            contactWhatsapp = "32999887766"
                        )
                    )
                )
            }
        }
    }
}
