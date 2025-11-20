package com.newyork.sharespace.api.controller

import com.newyork.sharespace.api.dto.*
import com.newyork.sharespace.config.exceptionHandling.ApiResponse
import com.newyork.sharespace.services.AuthService
import com.newyork.sharespace.services.entity.User
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(
        @Valid @RequestPart("user") body: RegisterRequest,
        @RequestPart("file") file: MultipartFile?
    ): ResponseEntity<ApiResponse<User>> {
        val user = authService.register(body, file)

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

    @PostMapping("/change-password")
    fun changePassword(
        @Valid @RequestBody body: ChangePasswordRequest
    ): ResponseEntity<ApiResponse<String>> {

        val userId = com.newyork.sharespace.config.JwtAuthFilter.getUserId()
            ?: return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"))

        authService.changePassword(userId, body)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = "Password updated successfully",
                message = "Password updated successfully"
            )
        )
    }

}