package com.newyork.sharespace.services

import com.newyork.sharespace.api.dto.AuthResponse
import com.newyork.sharespace.api.dto.LoginRequest
import com.newyork.sharespace.api.dto.RegisterRequest
import com.newyork.sharespace.config.HashEncoder
import com.newyork.sharespace.repository.RefreshTokenRepository
import com.newyork.sharespace.repository.UserRepository
import com.newyork.sharespace.services.entity.Gender
import com.newyork.sharespace.services.entity.RefreshToken
import com.newyork.sharespace.services.entity.User
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder,
    private val refreshTokenRepository: RefreshTokenRepository
) {


    fun register(
        request: RegisterRequest,
    ): User {
        userRepository.findByEmail(request.email.trim())?.let {
            throw ResponseStatusException(HttpStatus.CONFLICT, "A user with that email already exists.")
        }

        request.phoneNumber.takeIf { it.isNotBlank() }?.let { pn ->
            userRepository.findByPhoneNumber(pn)?.let {
                throw ResponseStatusException(HttpStatus.CONFLICT, "A user with that phone number already exists.")
            }
        }

        val genderEnum: Gender = try {
            Gender.valueOf(request.gender.trim().uppercase())
        } catch (ex: IllegalArgumentException) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid gender value. Allowed: ${Gender.values().joinToString(", ")}"
            )
        }

        val hashed = hashEncoder.encode(request.password)

        val user = User(
            email = request.email,
            password = hashed,
            name = request.name,
            phoneNumber = request.phoneNumber,
            gender = genderEnum,
            imageUrl = request.imageUrl,
            bio = request.bio
        )

        return userRepository.save(user)
    }

    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByPhoneNumber(request.phoneNumber)
            ?: throw BadCredentialsException("Invalid credentials.")

        if (!hashEncoder.matches(request.password, user.password)) {
            throw BadCredentialsException("Invalid credentials.")
        }

        val newAccessToken = jwtService.generateAccessToken(user.id.toString())
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toString())

        storeRefreshToken(user.id, newRefreshToken)

        return AuthResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    @Transactional
    fun refresh(refreshToken: String): AuthResponse {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid refresh token.")
        }

        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(UUID.fromString(userId)).orElseThrow {
            ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid refresh token.")
        }

        val hashed = hashToken(refreshToken)
        refreshTokenRepository.findByUserIdAndHashedToken(user.id, hashed)
            ?: throw ResponseStatusException(
                HttpStatusCode.valueOf(401),
                "Refresh token not recognized (maybe used or expired?)"
            )

        refreshTokenRepository.deleteByUserIdAndHashedToken(user.id, hashed)

        val newAccessToken = jwtService.generateAccessToken(userId)
        val newRefreshToken = jwtService.generateRefreshToken(userId)

        storeRefreshToken(user.id, newRefreshToken)

        return AuthResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    private fun storeRefreshToken(userId: UUID, rawRefreshToken: String) {
        val hashed = hashToken(rawRefreshToken)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt: LocalDateTime = LocalDateTime.now().plus(expiryMs, ChronoUnit.MILLIS)

        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashed
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}