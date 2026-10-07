package br.com.adotamatch.features.adopter.repository

import br.com.adotamatch.features.adopter.domain.CAdoptionIntent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @description Repositório Spring Data JPA para manifestações formais de interesse de adoção.
 */
@Repository
interface IAdoptionIntentRepository : JpaRepository<CAdoptionIntent, Long> {

    /**
     * @description Busca manifestações de interesse direcionadas a um determinado animal.
     * @param pPetId Identificador do animal.
     * @returns Lista de intenções de adoção registradas para o animal.
     */
    fun findByPetIdOrderByCompatibilityScoreDesc(pPetId: Long): List<CAdoptionIntent>
}
