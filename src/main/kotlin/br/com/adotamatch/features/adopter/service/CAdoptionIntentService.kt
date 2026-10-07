package br.com.adotamatch.features.adopter.service

import br.com.adotamatch.features.adopter.dto.CAdoptionIntentRequest
import br.com.adotamatch.features.adopter.dto.CAdoptionIntentResponse
import br.com.adotamatch.features.adopter.repository.IAdoptionIntentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @description Serviço responsável pela recepção e gestão de manifestações de interesse de adoção.
 */
@Service
class CAdoptionIntentService(
    private val adoptionIntentRepository: IAdoptionIntentRepository
) {

    /**
     * @description Registra formalmente a intenção de adoção de um usuário por um animal.
     * @param pRequest Dados do adotante e mensagem de sensibilização.
     * @returns Confirmação com protocolo de solicitação.
     */
    @Transactional
    fun submitIntent(pRequest: CAdoptionIntentRequest): CAdoptionIntentResponse {
        val entity = pRequest.toEntity()
        val saved = adoptionIntentRepository.save(entity)
        return CAdoptionIntentResponse.fromEntity(saved)
    }

    /**
     * @description Lista todas as manifestações de interesse para um determinado pet.
     * @param pPetId Identificador do animal.
     * @returns Lista de intenções ordenadas por maior score de compatibilidade.
     */
    @Transactional(readOnly = true)
    fun findByPetId(pPetId: Long): List<CAdoptionIntentResponse> {
        val intents = adoptionIntentRepository.findByPetIdOrderByCompatibilityScoreDesc(pPetId)
        return intents.map { CAdoptionIntentResponse.fromEntity(it) }
    }
}
