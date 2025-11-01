package com.newyork.sharespace.api.dto

import jakarta.validation.constraints.Pattern

data class LoginRequest(
    @field:Pattern(
        regexp = """^(\+\d{9,15})$""",
        message = "Invalid phone number format. Example: +201234567890"
    )
    val phone: String,
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
        message = "Password must be at least 8 characters long and contain at least one digit, uppercase and lowercase character."
    )
    val password: String
)
