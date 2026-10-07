package br.com.adotamatch.features.pet.dto

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.pet.domain.EPetGender
import br.com.adotamatch.features.pet.domain.EPetSize
import br.com.adotamatch.features.pet.domain.EPetStatus
import br.com.adotamatch.features.pet.domain.ESpecies

/**
 * @description DTO de saída detalhado com todos os atributos cadastrais e comportamentais do animal.
 * @property id Identificador único do animal.
 * @property name Nome do animal.
 * @property species Espécie (Cão ou Gato).
 * @property gender Sexo biológico (Macho ou Fêmea).
 * @property size Porte físico.
 * @property ageMonths Idade estimada em meses.
 * @property status Status atual de disponibilidade.
 * @property energyLevel Nível de energia (1 a 5).
 * @property solitaryToleranceHours Tolerância máxima à solidão em horas por dia.
 * @property kidsFriendly Indicador de boa convivência com crianças.
 * @property petsFriendly Indicador de boa convivência com outros pets.
 * @property requiresSpecialCare Indicador de necessidades especiais ou cuidados contínuos.
 * @property isVaccinated Indicador de vacinação em dia.
 * @property isNeutered Indicador de castração.
 * @property photoUrl URL da foto principal do animal.
 * @property additionalPhotos URLs de fotos adicionais da galeria.
 * @property videoUrl URL de vídeo de demonstração comportamental.
 * @property biography História e biografia afetiva do animal.
 * @property behavioralNotes Diário de observações comportamentais da instituição.
 * @property shelterName Nome da ONG, abrigo ou pet shop mantenedor.
 * @property shelterCity Cidade e estado do abrigo.
 * @property contactWhatsapp Número de WhatsApp para contato da adoção.
 */
data class CPetResponse(
    val id: Long,
    val name: String,
    val species: ESpecies,
    val gender: EPetGender,
    val size: EPetSize,
    val ageMonths: Int,
    val status: EPetStatus,
    val energyLevel: Int,
    val solitaryToleranceHours: Int,
    val kidsFriendly: Boolean,
    val petsFriendly: Boolean,
    val requiresSpecialCare: Boolean,
    val isVaccinated: Boolean,
    val isNeutered: Boolean,
    val photoUrl: String,
    val additionalPhotos: String?,
    val videoUrl: String?,
    val biography: String,
    val behavioralNotes: String,
    val shelterName: String,
    val shelterCity: String,
    val contactWhatsapp: String
) {
    companion object {
        /**
         * @description Converte uma entidade de domínio CPet em DTO CPetResponse.
         * @param pPet Entidade CPet persistida.
         * @returns Objeto CPetResponse formatado.
         */
        fun fromEntity(pPet: CPet): CPetResponse {
            return CPetResponse(
                id = pPet.id ?: 0L,
                name = pPet.name,
                species = pPet.species,
                gender = pPet.gender,
                size = pPet.size,
                ageMonths = pPet.ageMonths,
                status = pPet.status,
                energyLevel = pPet.energyLevel,
                solitaryToleranceHours = pPet.solitaryToleranceHours,
                kidsFriendly = pPet.kidsFriendly,
                petsFriendly = pPet.petsFriendly,
                requiresSpecialCare = pPet.requiresSpecialCare,
                isVaccinated = pPet.isVaccinated,
                isNeutered = pPet.isNeutered,
                photoUrl = pPet.photoUrl,
                additionalPhotos = pPet.additionalPhotos,
                videoUrl = pPet.videoUrl,
                biography = pPet.biography,
                behavioralNotes = pPet.behavioralNotes,
                shelterName = pPet.shelterName,
                shelterCity = pPet.shelterCity,
                contactWhatsapp = pPet.contactWhatsapp
            )
        }
    }
}
