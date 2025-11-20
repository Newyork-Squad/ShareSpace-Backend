package com.newyork.sharespace.api.dto

import java.time.LocalDateTime
import java.util.UUID

data class ReviewResponse(
    val id: UUID,
    val userId: UUID,
    val rating: Int,
    val comment: String,
    val createdAt: LocalDateTime
)