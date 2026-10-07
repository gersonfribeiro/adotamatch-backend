package br.com.adotamatch.features.pet.dto

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.pet.domain.EPetGender
import br.com.adotamatch.features.pet.domain.EPetSize
import br.com.adotamatch.features.pet.domain.EPetStatus
import br.com.adotamatch.features.pet.domain.ESpecies
import jakarta.validation.constraints.*

/**
 * @description DTO de entrada para cadastro (dar entrada) de um novo animal por uma ONG ou Pet Shop.
 * @property name Nome do animal.
 * @property species Espécie (DOG ou CAT).
 * @property gender Sexo biológico do animal.
 * @property size Porte físico.
 * @property ageMonths Idade estimada em meses.
 * @property energyLevel Nível de energia de 1 (muito calmo) a 5 (hiperativo).
 * @property solitaryToleranceHours Quantidade máxima de horas que o animal suporta ficar desacompanhado.
 * @property kidsFriendly Indicador se o animal aceita e convive bem com crianças.
 * @property petsFriendly Indicador se o animal convive bem com outros animais.
 * @property requiresSpecialCare Indicador de necessidades especiais de saúde ou tratamentos.
 * @property isVaccinated Indicador de vacinação em dia.
 * @property isNeutered Indicador de castração realizada.
 * @property photoUrl URL da foto principal de divulgação.
 * @property additionalPhotos URLs adicionais separadas por vírgula para compor galeria.
 * @property videoUrl URL de vídeo demonstrativo (YouTube ou link direto).
 * @property biography História do animal e perfil para sensibilização afetiva.
 * @property behavioralNotes Diário de notas comportamentais detalhadas da instituição para o motor de IA.
 * @property shelterName Nome da ONG, abrigo ou pet shop mantenedor.
 * @property shelterCity Cidade e UF onde o animal está abrigado.
 * @property contactWhatsapp Número de WhatsApp para contato direto dos interessados.
 */
data class CPetCreateRequest(
    @field:NotBlank(message = "O nome do animal é obrigatório.")
    val name: String,

    @field:NotNull(message = "A espécie é obrigatória.")
    val species: ESpecies,

    @field:NotNull(message = "O sexo biológico é obrigatório.")
    val gender: EPetGender = EPetGender.MALE,

    @field:NotNull(message = "O porte do animal é obrigatório.")
    val size: EPetSize,

    @field:Min(value = 1, message = "A idade mínima deve ser de pelo menos 1 mês.")
    val ageMonths: Int = 12,

    @field:Min(value = 1, message = "O nível de energia deve ser entre 1 e 5.")
    @field:Max(value = 5, message = "O nível de energia deve ser entre 1 e 5.")
    val energyLevel: Int = 3,

    @field:Min(value = 1, message = "A tolerância à solidão deve ser de pelo menos 1 hora.")
    @field:Max(value = 16, message = "A tolerância à solidão não pode exceder 16 horas.")
    val solitaryToleranceHours: Int = 6,

    val kidsFriendly: Boolean = true,

    val petsFriendly: Boolean = true,

    val requiresSpecialCare: Boolean = false,

    val isVaccinated: Boolean = true,

    val isNeutered: Boolean = true,

    @field:NotBlank(message = "A URL da foto principal é obrigatória.")
    val photoUrl: String,

    val additionalPhotos: String? = null,

    val videoUrl: String? = null,

    @field:NotBlank(message = "A biografia e história do animal são obrigatórias.")
    val biography: String,

    @field:NotBlank(message = "O diário comportamental é obrigatório para processamento pela IA.")
    val behavioralNotes: String,

    @field:NotBlank(message = "O nome da entidade mantenedora é obrigatório.")
    val shelterName: String,

    @field:NotBlank(message = "A cidade do mantenedor é obrigatória.")
    val shelterCity: String,

    @field:NotBlank(message = "O telefone de contato/WhatsApp é obrigatório.")
    val contactWhatsapp: String
) {
    /**
     * @description Converte o payload de criação em uma entidade de domínio CPet.
     * @returns Nova instância da entidade CPet.
     */
    fun toEntity(): CPet {
        return CPet(
            name = name.trim(),
            species = species,
            gender = gender,
            size = size,
            ageMonths = ageMonths,
            status = EPetStatus.AVAILABLE,
            energyLevel = energyLevel,
            solitaryToleranceHours = solitaryToleranceHours,
            kidsFriendly = kidsFriendly,
            petsFriendly = petsFriendly,
            requiresSpecialCare = requiresSpecialCare,
            isVaccinated = isVaccinated,
            isNeutered = isNeutered,
            photoUrl = photoUrl.trim(),
            additionalPhotos = additionalPhotos?.trim(),
            videoUrl = videoUrl?.trim(),
            biography = biography.trim(),
            behavioralNotes = behavioralNotes.trim(),
            shelterName = shelterName.trim(),
            shelterCity = shelterCity.trim(),
            contactWhatsapp = contactWhatsapp.trim()
        )
    }
}
