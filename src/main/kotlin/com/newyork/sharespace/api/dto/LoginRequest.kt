package com.newyork.sharespace.api.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:NotBlank(message = "Phone number is required")
    @field:Pattern(
        regexp = "^\\+?[1-9]\\d{1,14}$",
        message = "Phone number must be valid (E.164 format)"
    )
    val phoneNumber: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    val password: String
)
