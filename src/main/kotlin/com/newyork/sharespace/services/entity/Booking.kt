package com.newyork.sharespace.services.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "bookings")
data class Booking(
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    val workspace: Workspace,

    @Column(name = "user_id", columnDefinition = "uuid", nullable = false)
    val userId: UUID,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "end_time", nullable = false)
    val endTime: LocalDateTime,

    @Column(nullable = false)
    val duration: Double,

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    val paymentType: PaymentType,

    @Column(nullable = false)
    val cost: Double,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: BookingStatus = BookingStatus.PENDING,

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = true)
    val rateId: Double? = null
)

enum class PaymentType {
    CASH,
    CREDIT_CARD,
    DEBIT_CARD,
    PAYPAL,
    WALLET
}

enum class BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED
}

enum class PaymentStatus {
    UNPAID,
    PAID,
    REFUNDED
}
