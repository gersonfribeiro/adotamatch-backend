package br.com.adotamatch.features.adopter.controller

import br.com.adotamatch.features.adopter.dto.CAdoptionIntentRequest
import br.com.adotamatch.features.adopter.dto.CAdoptionIntentResponse
import br.com.adotamatch.features.adopter.service.CAdoptionIntentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * @description Controller REST para registro de intenções e manifestações de interesse na adoção.
 */
@RestController
@RequestMapping("/api/v1/adoptions")
@CrossOrigin(origins = ["*"])
class CAdoptionIntentController(
    private val adoptionIntentService: CAdoptionIntentService
) {

    /**
     * @description Submete uma manifestação de interesse de adoção.
     * @param pRequest Dados do adotante, justificativa e escore alcançado.
     * @returns Resposta HTTP 201 Created com o protocolo gerado.
     */
    @PostMapping("/intent")
    fun submitIntent(@Valid @RequestBody pRequest: CAdoptionIntentRequest): ResponseEntity<CAdoptionIntentResponse> {
        val response = adoptionIntentService.submitIntent(pRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    /**
     * @description Lista os interessados em um determinado animal para a ONG ou Pet Shop.
     * @param pPetId Identificador do animal.
     * @returns Resposta HTTP contendo a lista de interessados ranqueada por afinidade.
     */
    @GetMapping("/intent/pet/{pPetId}")
    fun findByPetId(@PathVariable pPetId: Long): ResponseEntity<List<CAdoptionIntentResponse>> {
        val intents = adoptionIntentService.findByPetId(pPetId)
        return ResponseEntity.ok(intents)
    }
}
