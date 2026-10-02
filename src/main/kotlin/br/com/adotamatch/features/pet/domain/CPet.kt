package br.com.adotamatch.features.pet.domain

import jakarta.persistence.*

/**
 * @description Enumeração de espécies suportadas pelo AdotaMatch.
 */
enum class ESpecies {
    DOG,
    CAT
}

/**
 * @description Porte físico do animal.
 */
enum class EPetSize {
    MINI,
    SMALL,
    MEDIUM,
    LARGE
}

/**
 * @description Status de disponibilidade do animal no abrigo.
 */
enum class EPetStatus {
    AVAILABLE,
    ADOPTED,
    UNDER_TREATMENT
}

/**
 * @description Entidade de domínio JPA que representa um animal sob custódia de abrigo/ONG.
 * @property id Identificador único no banco de dados.
 * @property name Nome do animal.
 * @property species Espécie (Cão ou Gato).
 * @property size Porte físico.
 * @property status Status atual de disponibilidade.
 * @property energyLevel Nível de energia de 1 (muito calmo) a 5 (hiperativo).
 * @property solitaryToleranceHours Quantidade máxima de horas que tolera permanecer sozinho.
 * @property kidsFriendly Tolerância comprovada a crianças.
 * @property petsFriendly Tolerância comprovada a outros animais.
 * @property requiresSpecialCare Indica se possui necessidades médicas ou cuidados contínuos.
 * @property behavioralNotes Diário comportamental em texto livre mantido pelos cuidadores.
 * @property shelterName Nome da instituição mantenedora.
 * @property shelterCity Cidade onde o animal se encontra.
 */
@Entity
@Table(name = "pets")
class CPet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var species: ESpecies = ESpecies.DOG,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var size: EPetSize = EPetSize.MEDIUM,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: EPetStatus = EPetStatus.AVAILABLE,

    @Column(nullable = false)
    var energyLevel: Int = 3,

    @Column(nullable = false)
    var solitaryToleranceHours: Int = 6,

    @Column(nullable = false)
    var kidsFriendly: Boolean = true,

    @Column(nullable = false)
    var petsFriendly: Boolean = true,

    @Column(nullable = false)
    var requiresSpecialCare: Boolean = false,

    @Column(columnDefinition = "TEXT")
    var behavioralNotes: String = "",

    @Column(nullable = false)
    var shelterName: String = "Abrigo Esperança Animal",

    @Column(nullable = false)
    var shelterCity: String = "Ubá"
)
