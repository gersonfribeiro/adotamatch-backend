package br.com.adotamatch.features.recommendation.controller

import br.com.adotamatch.features.recommendation.dto.CRecommendationRequest
import br.com.adotamatch.features.recommendation.dto.CRecommendationResponse
import br.com.adotamatch.features.recommendation.service.CRecommendationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @description Controller REST responsável pelo endpoint principal de recomendação inteligente.
 */
@RestController
@RequestMapping("/api/v1/recommendations")
@Tag(name = "Recomendação Inteligente", description = "Endpoints do Motor de Compatibilidade e Recomendação Explicável")
class CRecommendationController(
    private val recommendationService: CRecommendationService
) {

    /**
     * @description Calcula a compatibilidade entre o adotante e os animais disponíveis sob custódia.
     * @param pRequest Dados estruturados e relato de estilo de vida do adotante.
     * @returns Lista ordenada de animais com pontuações e explicações preventivas.
     */
    @PostMapping("/match")
    @Operation(
        summary = "Calcular Recomendações Responsáveis",
        description = "Aplica filtros rígidos, alinhamento semântico com LLM e pontuação ponderada multicritério para ranquear animais."
    )
    fun match(
        @Valid @RequestBody pRequest: CRecommendationRequest
    ): ResponseEntity<List<CRecommendationResponse>> {
        val recommendations = recommendationService.findMatches(pRequest)
        return ResponseEntity.ok(recommendations)
    }
}
