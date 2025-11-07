package com.newyork.sharespace.api.dto

import com.newyork.sharespace.services.entity.Amenities
import java.util.UUID

data class AddWorkspaceRequest(
    val title: String,
    val description: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val price: Double,
    val amenities: Set<Amenities> = emptySet(),
    val owner: UUID,
    val images: String = "",
    val reviews: String = "",
    val houseRules: String = "",
    val capacity: Int = 1,
    val rating: Double = 0.0,
    val reviewsNo: Int = 0
)
