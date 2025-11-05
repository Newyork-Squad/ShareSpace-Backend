package com.newyork.sharespace.config // <-- Use your package name

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    info = Info(
        title = "Share Space Backend API",
        version = "v1.0",
        description = "Documentation for the Spring Boot Kotlin backend for the Share Space application."
    )
)
@Configuration
class OpenApiConfig