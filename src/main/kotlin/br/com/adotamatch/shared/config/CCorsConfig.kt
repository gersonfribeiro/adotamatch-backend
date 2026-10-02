package br.com.adotamatch.shared.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @description Configuração de CORS para permitir comunicação do frontend Vue 3.
 */
@Configuration
class CCorsConfig : WebMvcConfigurer {

    /**
     * @description Define regras permissivas de CORS para desenvolvimento local.
     * @param pRegistry Registro de mapeamentos CORS.
     */
    override fun addCorsMappings(pRegistry: CorsRegistry) {
        pRegistry.addMapping("/**")
            .allowedOrigins("http://localhost:5173", "http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}
