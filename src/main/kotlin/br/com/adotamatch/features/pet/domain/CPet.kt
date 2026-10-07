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
 * @description Sexo biológico do animal.
 */
enum class EPetGender {
    MALE,
    FEMALE
}

/**
 * @description Status de disponibilidade do animal no abrigo ou pet shop parceiro.
 */
enum class EPetStatus {
    AVAILABLE,
    ADOPTED,
    UNDER_TREATMENT
}

/**
 * @description Entidade de domínio JPA que representa um animal sob custódia de abrigo, ONG ou pet shop.
 * @property id Identificador único no banco de dados.
 * @property name Nome do animal.
 * @property species Espécie (Cão ou Gato).
 * @property gender Sexo biológico (Macho ou Fêmea).
 * @property size Porte físico.
 * @property ageMonths Idade estimada em meses.
 * @property status Status atual de disponibilidade.
 * @property energyLevel Nível de energia de 1 (muito calmo) a 5 (hiperativo).
 * @property solitaryToleranceHours Quantidade máxima de horas que tolera permanecer sozinho.
 * @property kidsFriendly Tolerância comprovada a crianças.
 * @property petsFriendly Tolerância comprovada a outros animais.
 * @property requiresSpecialCare Indica se possui necessidades médicas ou cuidados contínuos.
 * @property isVaccinated Indica se o ciclo de vacinação está atualizado.
 * @property isNeutered Indica se o animal já foi castrado.
 * @property photoUrl URL da foto principal de divulgação.
 * @property additionalPhotos URLs de fotos adicionais da galeria.
 * @property videoUrl URL de vídeo demonstrativo (YouTube, Vimeo ou MP4).
 * @property biography História do resgate e perfil afetivo do animal.
 * @property behavioralNotes Diário comportamental mantido pelos cuidadores para inferência de IA.
 * @property shelterName Nome da instituição mantenedora, ONG ou pet shop acolhedor.
 * @property shelterCity Cidade e UF onde o animal se encontra.
 * @property contactWhatsapp Número de WhatsApp para contato direto e formalização da adoção.
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
    var gender: EPetGender = EPetGender.MALE,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var size: EPetSize = EPetSize.MEDIUM,

    @Column(nullable = false)
    var ageMonths: Int = 12,

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

    @Column(nullable = false)
    var isVaccinated: Boolean = true,

    @Column(nullable = false)
    var isNeutered: Boolean = true,

    @Column(nullable = false, length = 1000)
    var photoUrl: String = "",

    @Column(length = 2000)
    var additionalPhotos: String? = null,

    @Column(length = 1000)
    var videoUrl: String? = null,

    @Column(columnDefinition = "TEXT", nullable = false)
    var biography: String = "",

    @Column(columnDefinition = "TEXT", nullable = false)
    var behavioralNotes: String = "",

    @Column(nullable = false)
    var shelterName: String = "Abrigo Esperança Animal",

    @Column(nullable = false)
    var shelterCity: String = "Ubá - MG",

    @Column(nullable = false)
    var contactWhatsapp: String = "32999999999"
)
