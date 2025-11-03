package com.newyork.sharespace.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must be valid")
    @field:Size(max = 255, message = "Email must not exceed 255 characters")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit"
    )
    val password: String,

    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String,

    @field:NotBlank(message = "Phone number is required")
    @field:Pattern(
        regexp = "^\\+?[1-9]\\d{1,14}$",
        message = "Phone number must be valid (E.164 format)"
    )
    val phoneNumber: String,

    @field:NotBlank(message = "Gender is required")
    @field:Pattern(
        regexp = "^(MALE|FEMALE|OTHER)$",
        message = "Gender must be one of: MALE, FEMALE, OTHER",
        flags = [Pattern.Flag.CASE_INSENSITIVE]
    )
    val gender: String,

    @field:Size(max = 500, message = "Image URL must not exceed 500 characters")
    val imageUrl: String? = null,

    @field:Size(max = 1000, message = "Bio must not exceed 1000 characters")
    val bio: String? = null
)
