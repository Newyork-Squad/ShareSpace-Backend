package com.newyork.sharespace.repository

import com.newyork.sharespace.services.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, UUID> {
    fun findByUserIdAndHashedToken(userId: UUID, hashedToken: String): RefreshToken?
    fun deleteByUserIdAndHashedToken(userId: UUID, hashedToken: String)
}