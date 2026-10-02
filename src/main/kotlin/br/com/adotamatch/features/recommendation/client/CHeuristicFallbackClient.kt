package br.com.adotamatch.features.recommendation.client

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.pet.domain.EPetSize
import br.com.adotamatch.features.recommendation.dto.CRecommendationRequest
import org.springframework.stereotype.Component

/**
 * @description Provedor de inferência semântica e fallback determinístico baseado em regras de PLN.
 * Garante disponibilidade integral caso APIs externas de LLM estejam indisponíveis ou excedam o timeout.
 */
@Component
class CHeuristicFallbackClient : ILlmClient {

    /**
     * @description Executa a avaliação contextual sintetizando afinidades e alertas preventivos de convivência.
     * @param pRequest Dados e narrativa do adotante.
     * @param pPet Dados do animal sob custódia.
     * @returns Resultado estruturado de alinhamento contextual.
     */
    override fun evaluate(pRequest: CRecommendationRequest, pPet: CPet): CLlmEvaluationResult {
        val narrativeLower = pRequest.lifestyleNarrative.lowercase()
        val factors = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        var semanticBonus = 0.75

        // Análise de rotina de trabalho / home office
        if (narrativeLower.contains("remot") || narrativeLower.contains("home office") || narrativeLower.contains("casa")) {
            factors.add("Sua rotina flexível ou trabalho remoto oferece a presença constante ideal para o temperamento de ${pPet.name}.")
            semanticBonus += 0.10
        } else if (pPet.solitaryToleranceHours < 5) {
            warnings.add("${pPet.name} requer companhia frequente e pode desenvolver ansiedade de separação caso fique sozinho por períodos extensos.")
            semanticBonus -= 0.10
        }

        // Análise de passeios e atividades físicas
        if (narrativeLower.contains("passei") || narrativeLower.contains("caminh") || narrativeLower.contains("corr")) {
            if (pPet.energyLevel >= 3) {
                factors.add("Seu interesse declarado por passeios diários harmoniza-se com a energia ativa (nível ${pPet.energyLevel}) de ${pPet.name}.")
                semanticBonus += 0.10
            }
        }

        // Alertas de adaptação com crianças
        if (pRequest.hasChildren) {
            if (pPet.kidsFriendly) {
                factors.add("O animal possui histórico positivo de convivência e paciência com crianças no abrigo.")
            } else {
                warnings.add("Recomenda-se supervisão atenta e processo gradual de aproximação com as crianças nas primeiras semanas.")
            }
        }

        // Alertas de espaço e contenção
        if (pPet.size == EPetSize.LARGE) {
            warnings.add("Por ser de porte grande, demanda enriquecimento ambiental e passeios regulares para prevenir comportamentos destrutivos.")
        }

        // Alertas veterinários preventivos
        if (pPet.requiresSpecialCare) {
            warnings.add("Atenção: este animal requer acompanhamento veterinário ou cuidados médicos específicos detalhados pelo abrigo.")
        }

        if (factors.isEmpty()) {
            factors.add("O temperamento de ${pPet.name} demonstra boa aderência às características informadas para a rotina familiar.")
        }

        if (warnings.isEmpty()) {
            warnings.add("Como em qualquer adoção, reserve os primeiros 15 a 30 dias para a fase de adaptação e criação de vínculo.")
        }

        val finalScore = semanticBonus.coerceIn(0.10, 1.00)
        return CLlmEvaluationResult(
            semanticScore = finalScore,
            alignmentFactors = factors,
            preventiveWarnings = warnings
        )
    }
}
