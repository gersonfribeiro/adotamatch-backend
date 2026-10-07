package br.com.adotamatch.features.pet.controller

import br.com.adotamatch.features.pet.domain.EPetStatus
import br.com.adotamatch.features.pet.domain.ESpecies
import br.com.adotamatch.features.pet.dto.CPetCreateRequest
import br.com.adotamatch.features.pet.dto.CPetResponse
import br.com.adotamatch.features.pet.dto.CPetUpdateRequest
import br.com.adotamatch.features.pet.service.CPetService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * @description Controller REST para gestão e catálogo de animais sob custódia de ONGs e Pet Shops parceiros.
 */
@RestController
@RequestMapping("/api/v1/pets")
@CrossOrigin(origins = ["*"])
class CPetController(
    private val petService: CPetService
) {

    /**
     * @description Lista animais disponíveis ou filtrados.
     * @param pSpecies Espécie opcional para filtro (DOG ou CAT).
     * @param pStatus Status opcional do animal.
     * @param pSearch Termo de busca textual.
     * @returns Resposta HTTP contendo a lista de animais cadastrados.
     */
    @GetMapping
    fun findAll(
        @RequestParam(required = false) pSpecies: ESpecies?,
        @RequestParam(required = false) pStatus: EPetStatus?,
        @RequestParam(required = false) pSearch: String?
    ): ResponseEntity<List<CPetResponse>> {
        val result = petService.findAll(pSpecies, pStatus, pSearch)
        return ResponseEntity.ok(result)
    }

    /**
     * @description Obtém os dados completos de um animal pelo identificador.
     * @param pId Identificador único do animal.
     * @returns Resposta HTTP contendo o DTO do animal.
     */
    @GetMapping("/{pId}")
    fun findById(@PathVariable pId: Long): ResponseEntity<CPetResponse> {
        val pet = petService.findById(pId)
        return ResponseEntity.ok(pet)
    }

    /**
     * @description Cadastra (dá entrada em) um novo animal acolhido por um abrigo, ONG ou pet shop parceiro.
     * @param pRequest Dados cadastrais, mídias e notas comportamentais do animal.
     * @returns Resposta HTTP 201 Created com o animal criado.
     */
    @PostMapping
    fun create(@Valid @RequestBody pRequest: CPetCreateRequest): ResponseEntity<CPetResponse> {
        val created = petService.create(pRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    /**
     * @description Atualiza os dados de um animal existente.
     * @param pId Identificador único do animal.
     * @param pRequest Dados atualizados do animal.
     * @returns Resposta HTTP contendo o animal atualizado.
     */
    @PutMapping("/{pId}")
    fun update(
        @PathVariable pId: Long,
        @Valid @RequestBody pRequest: CPetUpdateRequest
    ): ResponseEntity<CPetResponse> {
        val updated = petService.update(pId, pRequest)
        return ResponseEntity.ok(updated)
    }

    /**
     * @description Remove ou desativa um animal do sistema.
     * @param pId Identificador único do animal.
     * @returns Resposta HTTP 204 No Content.
     */
    @DeleteMapping("/{pId}")
    fun delete(@PathVariable pId: Long): ResponseEntity<Void> {
        petService.delete(pId)
        return ResponseEntity.noContent().build()
    }
}
