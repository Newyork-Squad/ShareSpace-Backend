package com.newyork.sharespace.api.dto.booking

import com.newyork.sharespace.api.dto.workspace.WorkspaceResponse
import com.newyork.sharespace.api.dto.workspace.toWorkspaceResponse
import com.newyork.sharespace.services.entity.Booking
import com.newyork.sharespace.services.entity.BookingStatus
import com.newyork.sharespace.services.entity.PaymentStatus
import java.time.LocalDateTime
import java.util.*

data class BookingResponse(
    val id: UUID,
    val workspace: WorkspaceResponse,
    val userId: UUID,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val duration: Double,
    val paymentType: String,
    val cost: Double,
    val status: BookingStatus,
    val paymentStatus: PaymentStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

fun Booking.toBookingResponse() = BookingResponse(
    id = id,
    workspace = workspace.toWorkspaceResponse(),
    userId = userId,
    startTime = startTime,
    endTime = endTime,
    duration = duration,
    paymentType = paymentType.name,
    cost = cost,
    status = status,
    paymentStatus = paymentStatus,
    createdAt = createdAt,
    updatedAt = updatedAt
)