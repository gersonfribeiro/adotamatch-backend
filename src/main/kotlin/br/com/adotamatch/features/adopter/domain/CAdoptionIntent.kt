package br.com.adotamatch.features.adopter.domain

import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * @description Status do fluxo de análise de adoção.
 */
enum class EAdoptionStatus {
    SUBMITTED,
    IN_REVIEW,
    APPROVED,
    REJECTED
}

/**
 * @description Entidade de domínio JPA que registra a manifestação formal de interesse de um adotante por um animal específico.
 * @property id Identificador único do registro de interesse.
 * @property petId Identificador do animal de interesse.
 * @property petName Nome do animal.
 * @property adopterName Nome completo do adotante interessado.
 * @property adopterEmail E-mail de contato do adotante.
 * @property adopterPhone Telefone/WhatsApp do adotante.
 * @property adopterCity Cidade e estado de residência do adotante.
 * @property compatibilityScore Pontuação calculada pelo motor de recomendação no momento da manifestação.
 * @property adopterNarrative Relato livre de estilo de vida fornecido na avaliação.
 * @property messageToShelter Mensagem direta enviada para a equipe da ONG ou pet shop.
 * @property status Status da análise da solicitação.
 * @property createdAt Data e hora do envio da manifestação de interesse.
 */
@Entity
@Table(name = "adoption_intents")
class CAdoptionIntent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var petId: Long = 0L,

    @Column(nullable = false)
    var petName: String = "",

    @Column(nullable = false)
    var adopterName: String = "",

    @Column(nullable = false)
    var adopterEmail: String = "",

    @Column(nullable = false)
    var adopterPhone: String = "",

    @Column(nullable = false)
    var adopterCity: String = "",

    @Column(nullable = false)
    var compatibilityScore: Double = 0.0,

    @Column(columnDefinition = "TEXT", nullable = false)
    var adopterNarrative: String = "",

    @Column(columnDefinition = "TEXT", nullable = false)
    var messageToShelter: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: EAdoptionStatus = EAdoptionStatus.SUBMITTED,

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)
