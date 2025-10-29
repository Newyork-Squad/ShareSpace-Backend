package com.newyork.sharespace.services.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "notifications")
data class Notification(
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "user_id", columnDefinition = "uuid", nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val title: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    val message: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: NotificationType,

    @Column(name = "is_read", nullable = false)
    val isRead: Boolean = false,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class NotificationType {
    BOOKING_CONFIRMATION,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,
    BOOKING_CANCELLED,
    REVIEW_RECEIVED,
    PROMO_OFFER,
    GENERAL
}
