package br.com.adotamatch.features.pet.repository

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.pet.domain.EPetStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @description Repositório Spring Data JPA para gerenciamento da persistência de animais.
 */
@Repository
interface IPetRepository : JpaRepository<CPet, Long> {

    /**
     * @description Busca todos os animais com determinado status de disponibilidade.
     * @param pStatus Status desejado para filtragem.
     * @returns Lista de animais que correspondem ao status.
     */
    fun findByStatus(pStatus: EPetStatus): List<CPet>
}
