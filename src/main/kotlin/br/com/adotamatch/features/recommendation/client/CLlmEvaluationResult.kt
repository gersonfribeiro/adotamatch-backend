package br.com.adotamatch.features.recommendation.client

/**
 * @description Resultado estruturado da inferência semântica e contextual gerada pela LLM.
 * @property semanticScore Escore de afinidade normalizado entre 0.0 e 1.0.
 * @property alignmentFactors Lista de fatores que fundamentam a afinidade contextual.
 * @property preventiveWarnings Lista de alertas de conscientização e desafios de adaptação.
 */
data class CLlmEvaluationResult(
    val semanticScore: Double,
    val alignmentFactors: List<String>,
    val preventiveWarnings: List<String>
)
