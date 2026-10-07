package br.com.adotamatch.features.adopter.dto

import br.com.adotamatch.features.adopter.domain.CAdoptionIntent
import br.com.adotamatch.features.adopter.domain.EAdoptionStatus
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

/**
 * @description Payload de solicitação de manifestação de interesse por parte do adotante.
 * @property petId Identificador do animal desejado.
 * @property petName Nome do animal.
 * @property adopterName Nome completo do adotante.
 * @property adopterEmail E-mail de contato.
 * @property adopterPhone Telefone ou WhatsApp.
 * @property adopterCity Cidade e UF de residência.
 * @property compatibilityScore Pontuação calculada na recomendação.
 * @property adopterNarrative Relato de estilo de vida fornecido na triagem.
 * @property messageToShelter Mensagem pessoal para a ONG ou pet shop.
 */
data class CAdoptionIntentRequest(
    @field:NotNull(message = "O ID do animal é obrigatório.")
    val petId: Long,

    @field:NotBlank(message = "O nome do animal é obrigatório.")
    val petName: String,

    @field:NotBlank(message = "O nome do adotante é obrigatório.")
    val adopterName: String,

    @field:NotBlank(message = "O e-mail é obrigatório.")
    @field:Email(message = "O e-mail informado é inválido.")
    val adopterEmail: String,

    @field:NotBlank(message = "O telefone de contato é obrigatório.")
    val adopterPhone: String,

    @field:NotBlank(message = "A cidade é obrigatória.")
    val adopterCity: String,

    val compatibilityScore: Double = 0.0,

    val adopterNarrative: String = "",

    @field:NotBlank(message = "A mensagem para a ONG ou abrigo é obrigatória.")
    val messageToShelter: String
) {
    /**
     * @description Converte o payload na entidade de domínio CAdoptionIntent.
     * @returns Nova instância de CAdoptionIntent persistível.
     */
    fun toEntity(): CAdoptionIntent {
        return CAdoptionIntent(
            petId = petId,
            petName = petName.trim(),
            adopterName = adopterName.trim(),
            adopterEmail = adopterEmail.trim(),
            adopterPhone = adopterPhone.trim(),
            adopterCity = adopterCity.trim(),
            compatibilityScore = compatibilityScore,
            adopterNarrative = adopterNarrative.trim(),
            messageToShelter = messageToShelter.trim(),
            status = EAdoptionStatus.SUBMITTED,
            createdAt = LocalDateTime.now()
        )
    }
}

/**
 * @description Contrato de resposta confirmando o recebimento da manifestação de interesse.
 * @property id Identificador do protocolo gerado.
 * @property petId Identificador do animal.
 * @property petName Nome do animal.
 * @property adopterName Nome do adotante.
 * @property compatibilityScore Escore de compatibilidade.
 * @property status Status atual da solicitação.
 * @property createdAt Timestamp de criação do registro.
 */
data class CAdoptionIntentResponse(
    val id: Long,
    val petId: Long,
    val petName: String,
    val adopterName: String,
    val compatibilityScore: Double,
    val status: EAdoptionStatus,
    val createdAt: LocalDateTime
) {
    companion object {
        /**
         * @description Cria o DTO de resposta a partir da entidade CAdoptionIntent.
         * @param pEntity Entidade persistida.
         * @returns DTO de resposta preenchido.
         */
        fun fromEntity(pEntity: CAdoptionIntent): CAdoptionIntentResponse {
            return CAdoptionIntentResponse(
                id = pEntity.id ?: 0L,
                petId = pEntity.petId,
                petName = pEntity.petName,
                adopterName = pEntity.adopterName,
                compatibilityScore = pEntity.compatibilityScore,
                status = pEntity.status,
                createdAt = pEntity.createdAt
            )
        }
    }
}
