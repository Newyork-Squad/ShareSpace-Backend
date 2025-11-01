package com.newyork.sharespace.services.entity


import java.time.Instant
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "refresh_tokens")
data class RefreshToken(
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val hashedToken: String,

    @Column(nullable = false)
    val expiresAt: Instant,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now()
)

