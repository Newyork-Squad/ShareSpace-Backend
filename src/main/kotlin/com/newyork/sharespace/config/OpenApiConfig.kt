package com.newyork.sharespace.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    info = Info(
        title = "Share Space Backend API",
        version = "v1.0",
        description = "ShareSpace Workspace Booking API - Production"
    )
)
@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .servers(
                listOf(
                    Server()
                        .url("https://sharespace-backend-2834.onrender.com")
                        .description("Production Server")
                )
            )
            .addSecurityItem(SecurityRequirement().addList("bearer-key"))
            .components(
                Components()
                    .addSecuritySchemes(
                        "bearer-key", SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
    }
}