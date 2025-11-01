package com.newyork.sharespace.repository

import com.newyork.sharespace.services.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RefreshTokenRepository: JpaRepository<RefreshToken, UUID> {
    fun findByUserIdAndHashedToken(userId: UUID, hashedToken: String): RefreshToken?
    fun deleteByUserIdAndHashedToken(userId: UUID, hashedToken: String)
}