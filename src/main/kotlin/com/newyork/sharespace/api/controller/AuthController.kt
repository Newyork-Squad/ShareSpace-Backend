package com.newyork.sharespace.api.controller

import com.newyork.sharespace.api.dto.LoginRequest
import com.newyork.sharespace.api.dto.RefreshRequest
import com.newyork.sharespace.api.dto.RegisterRequest
import com.newyork.sharespace.services.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
    val authService : AuthService
) {
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody body: RegisterRequest
    ) {
        authService.register(body)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody body: LoginRequest
    ): AuthService.TokenPair {
        return authService.login(body.phone, body.password)
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody body: RefreshRequest
    ): AuthService.TokenPair {
        return authService.refresh(body.refreshToken)
    }



}