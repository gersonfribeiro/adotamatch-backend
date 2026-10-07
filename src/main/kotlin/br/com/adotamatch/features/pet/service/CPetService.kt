package br.com.adotamatch.features.pet.service

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.pet.domain.EPetStatus
import br.com.adotamatch.features.pet.domain.ESpecies
import br.com.adotamatch.features.pet.dto.CPetCreateRequest
import br.com.adotamatch.features.pet.dto.CPetResponse
import br.com.adotamatch.features.pet.dto.CPetUpdateRequest
import br.com.adotamatch.features.pet.repository.IPetRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

/**
 * @description Serviço responsável pelas operações de gestão do ciclo de vida dos animais mantidos por abrigos e pet shops.
 */
@Service
class CPetService(
    private val petRepository: IPetRepository
) {

    /**
     * @description Lista animais com filtros opcionais de espécie, status e busca textual.
     * @param pSpecies Espécie opcional para filtro.
     * @param pStatus Status opcional para filtro.
     * @param pSearch Termo de busca opcional por nome, abrigo ou cidade.
     * @returns Lista de animais mapeados para DTOs.
     */
    @Transactional(readOnly = true)
    fun findAll(pSpecies: ESpecies?, pStatus: EPetStatus?, pSearch: String?): List<CPetResponse> {
        val allPets = petRepository.findAll()

        return allPets
            .filter { pet -> pSpecies == null || pet.species == pSpecies }
            .filter { pet -> pStatus == null || pet.status == pStatus }
            .filter { pet ->
                if (pSearch.isNullOrBlank()) true
                else {
                    val term = pSearch.trim().lowercase()
                    pet.name.lowercase().contains(term) ||
                    pet.shelterName.lowercase().contains(term) ||
                    pet.shelterCity.lowercase().contains(term)
                }
            }
            .map { CPetResponse.fromEntity(it) }
    }

    /**
     * @description Recupera um animal pelo identificador único.
     * @param pId Identificador único do animal.
     * @returns DTO com os detalhes completos do animal.
     */
    @Transactional(readOnly = true)
    fun findById(pId: Long): CPetResponse {
        val pet = petRepository.findById(pId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Animal com id $pId não encontrado.")
        }
        return CPetResponse.fromEntity(pet)
    }

    /**
     * @description Cadastra (dá entrada em) um novo animal acolhido por um abrigo, ONG ou pet shop parceiro.
     * @param pRequest Dados cadastrais, mídias e diário comportamental.
     * @returns DTO do animal recém-cadastrado.
     */
    @Transactional
    fun create(pRequest: CPetCreateRequest): CPetResponse {
        val newPet = pRequest.toEntity()
        val saved = petRepository.save(newPet)
        return CPetResponse.fromEntity(saved)
    }

    /**
     * @description Atualiza os dados de um animal existente.
     * @param pId Identificador único do animal.
     * @param pRequest Dados atualizados do animal.
     * @returns DTO do animal com as alterações persistidas.
     */
    @Transactional
    fun update(pId: Long, pRequest: CPetUpdateRequest): CPetResponse {
        val existingPet = petRepository.findById(pId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Animal com id $pId não encontrado.")
        }
        pRequest.applyToEntity(existingPet)
        val updated = petRepository.save(existingPet)
        return CPetResponse.fromEntity(updated)
    }

    /**
     * @description Remove ou desativa um animal do catálogo.
     * @param pId Identificador único do animal.
     */
    @Transactional
    fun delete(pId: Long) {
        if (!petRepository.existsById(pId)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Animal com id $pId não encontrado.")
        }
        petRepository.deleteById(pId)
    }
}
