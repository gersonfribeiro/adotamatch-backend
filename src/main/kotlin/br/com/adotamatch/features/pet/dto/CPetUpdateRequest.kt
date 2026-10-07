package br.com.adotamatch.features.pet.dto

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.pet.domain.EPetGender
import br.com.adotamatch.features.pet.domain.EPetSize
import br.com.adotamatch.features.pet.domain.EPetStatus
import br.com.adotamatch.features.pet.domain.ESpecies
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * @description DTO para atualização cadastral e de status do animal pelo mantenedor.
 * @property name Nome do animal.
 * @property species Espécie do animal.
 * @property gender Sexo biológico do animal.
 * @property size Porte físico.
 * @property ageMonths Idade estimada em meses.
 * @property status Status de disponibilidade do animal.
 * @property energyLevel Nível de energia (1 a 5).
 * @property solitaryToleranceHours Tolerância máxima à solidão em horas por dia.
 * @property kidsFriendly Indicador de sociabilidade com crianças.
 * @property petsFriendly Indicador de sociabilidade com outros pets.
 * @property requiresSpecialCare Necessidades médicas especiais.
 * @property isVaccinated Indicador de vacinação.
 * @property isNeutered Indicador de castração.
 * @property photoUrl URL da foto principal.
 * @property additionalPhotos URLs de fotos adicionais.
 * @property videoUrl URL de vídeo demonstrativo.
 * @property biography História e perfil afetivo.
 * @property behavioralNotes Diário comportamental para IA.
 * @property shelterName Nome do mantenedor.
 * @property shelterCity Cidade do mantenedor.
 * @property contactWhatsapp Contato WhatsApp.
 */
data class CPetUpdateRequest(
    @field:NotBlank(message = "O nome é obrigatório.")
    val name: String,

    @field:NotNull(message = "A espécie é obrigatória.")
    val species: ESpecies,

    @field:NotNull(message = "O sexo biológico é obrigatório.")
    val gender: EPetGender,

    @field:NotNull(message = "O porte é obrigatório.")
    val size: EPetSize,

    @field:Min(value = 1, message = "A idade mínima deve ser de 1 mês.")
    val ageMonths: Int,

    @field:NotNull(message = "O status é obrigatório.")
    val status: EPetStatus,

    @field:Min(value = 1, message = "O nível de energia deve ser entre 1 e 5.")
    @field:Max(value = 5, message = "O nível de energia deve ser entre 1 e 5.")
    val energyLevel: Int,

    @field:Min(value = 1, message = "A tolerância à solidão deve ser de pelo menos 1 hora.")
    @field:Max(value = 16, message = "A tolerância à solidão não pode exceder 16 horas.")
    val solitaryToleranceHours: Int,

    val kidsFriendly: Boolean,

    val petsFriendly: Boolean,

    val requiresSpecialCare: Boolean,

    val isVaccinated: Boolean,

    val isNeutered: Boolean,

    @field:NotBlank(message = "A foto principal é obrigatória.")
    val photoUrl: String,

    val additionalPhotos: String?,

    val videoUrl: String?,

    @field:NotBlank(message = "A biografia é obrigatória.")
    val biography: String,

    @field:NotBlank(message = "O diário comportamental é obrigatório.")
    val behavioralNotes: String,

    @field:NotBlank(message = "O nome do abrigo/mantenedor é obrigatório.")
    val shelterName: String,

    @field:NotBlank(message = "A cidade do abrigo é obrigatória.")
    val shelterCity: String,

    @field:NotBlank(message = "O contato de WhatsApp é obrigatório.")
    val contactWhatsapp: String
) {
    /**
     * @description Aplica os dados atualizados sobre uma entidade CPet existente.
     * @param pPet Entidade a ser atualizada.
     */
    fun applyToEntity(pPet: CPet) {
        pPet.name = name.trim()
        pPet.species = species
        pPet.gender = gender
        pPet.size = size
        pPet.ageMonths = ageMonths
        pPet.status = status
        pPet.energyLevel = energyLevel
        pPet.solitaryToleranceHours = solitaryToleranceHours
        pPet.kidsFriendly = kidsFriendly
        pPet.petsFriendly = petsFriendly
        pPet.requiresSpecialCare = requiresSpecialCare
        pPet.isVaccinated = isVaccinated
        pPet.isNeutered = isNeutered
        pPet.photoUrl = photoUrl.trim()
        pPet.additionalPhotos = additionalPhotos?.trim()
        pPet.videoUrl = videoUrl?.trim()
        pPet.biography = biography.trim()
        pPet.behavioralNotes = behavioralNotes.trim()
        pPet.shelterName = shelterName.trim()
        pPet.shelterCity = shelterCity.trim()
        pPet.contactWhatsapp = contactWhatsapp.trim()
    }
}
