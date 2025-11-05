package com.newyork.sharespace.api.controller

import com.newyork.sharespace.api.dto.AuthResponse
import com.newyork.sharespace.api.dto.LoginRequest
import com.newyork.sharespace.api.dto.RefreshRequest
import com.newyork.sharespace.api.dto.RegisterRequest
import com.newyork.sharespace.config.exceptionHandling.ApiResponse
import com.newyork.sharespace.services.AuthService
import com.newyork.sharespace.services.entity.User
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody body: RegisterRequest
    ): ResponseEntity<ApiResponse<User>> {
        val user = authService.register(body)

        val response = ApiResponse.success(
            data = user,
            message = "User registered successfully"
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody body: LoginRequest
    ): ResponseEntity<ApiResponse<AuthResponse>> {
        val authResponse = authService.login(body)

        val response = ApiResponse.success(
            data = authResponse,
            message = "Login successful"
        )

        return ResponseEntity.ok(response)
    }

    @PostMapping("/refresh")
    fun refresh(
        @Valid @RequestBody body: RefreshRequest
    ): ResponseEntity<ApiResponse<AuthResponse>> {
        val authResponse = authService.refresh(body.refreshToken)

        val response = ApiResponse.success(
            data = authResponse,
            message = "Token refreshed successfully"
        )

        return ResponseEntity.ok(response)
    }
}