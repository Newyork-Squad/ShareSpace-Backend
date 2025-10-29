package com.newyork.sharespace.services.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "reviews")
data class Review(
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),


    @Column(columnDefinition = "TEXT", nullable = false)
    val description: String,


    @Column(name = "user_id", columnDefinition = "uuid", nullable = false)
    val userId: UUID,


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    val workspace: Workspace,


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    val booking: Booking,


    @Column(nullable = false)
    val rate: Double,


    @Column(nullable = false)
    val date: LocalDateTime = LocalDateTime.now()
)
