package br.com.adotamatch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @description Ponto de entrada do sistema AdotaMatch (Spring Boot com Kotlin).
 */
@SpringBootApplication
class CAdotaMatchApplication

fun main(args: Array<String>) {
    runApplication<CAdotaMatchApplication>(*args)
}
