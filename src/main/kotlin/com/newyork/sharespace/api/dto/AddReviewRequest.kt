package com.newyork.sharespace.api.dto

import java.util.UUID

data class AddReviewRequest(
    val workspaceId: UUID,
    val rating: Int,
    val comment: String
)