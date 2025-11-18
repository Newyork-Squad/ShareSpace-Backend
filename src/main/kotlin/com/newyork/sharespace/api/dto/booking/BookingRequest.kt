package com.newyork.sharespace.api.dto.booking

import com.newyork.sharespace.services.entity.PaymentType
import java.util.*

data class BookingRequest(
    val workspaceId: UUID,
    val date: String,
    val startTime: String,
    val durationHours: Double,
    val paymentType: PaymentType
)