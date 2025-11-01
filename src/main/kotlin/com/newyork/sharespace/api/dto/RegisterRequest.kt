package com.newyork.sharespace.api.dto

import com.newyork.sharespace.services.entity.Gender
import jakarta.validation.constraints.*

data class RegisterRequest(
    @field:NotBlank(message = "Full name must not be blank")
    val fullName: String,

    @field:Pattern(
        regexp = "^[+]?[0-9\\s-]{9,15}$",
        message = "Invalid phone number format"
    )
    val phone: String,

    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email must not be blank")
    val email: String,

    val bio: String? = null,

    @field:NotNull(message = "Gender must be selected")
    val gender: Gender,

    @field:NotBlank(message = "Password must not be blank")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
        message = "Password must be at least 8 characters long and contain at least one digit, uppercase and lowercase character."
    )
    val password: String,

    @field:NotBlank(message = "Confirmation password must not be blank")
    val confirmPassword: String
)




