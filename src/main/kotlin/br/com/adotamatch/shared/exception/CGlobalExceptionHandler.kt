package br.com.adotamatch.shared.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * @description Tratador global de exceções para padronização de respostas de erro na API REST (RFC 7807).
 */
@RestControllerAdvice
class CGlobalExceptionHandler {

    /**
     * @description Trata falhas de validação dos payloads @Valid.
     * @param pEx Exceção disparada pelo Spring Validator.
     * @returns ProblemDetail com status 400 e os erros de validação mapeados.
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(pEx: MethodArgumentNotValidException): ProblemDetail {
        val problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
        problem.title = "Erro de Validação nos Dados de Entrada"

        val fieldErrors = pEx.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "Valor inválido")
        }
        problem.setProperty("invalidFields", fieldErrors)
        return problem
    }

    /**
     * @description Trata exceções não mapeadas do sistema.
     * @param pEx Exceção capturada em tempo de execução.
     * @returns ProblemDetail com status 500.
     */
    @ExceptionHandler(Exception::class)
    fun handleGenericException(pEx: Exception): ProblemDetail {
        val problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        problem.title = "Ocorreu um erro interno no servidor"
        problem.detail = pEx.message ?: "Erro inesperado ao processar a requisição."
        return problem
    }
}
