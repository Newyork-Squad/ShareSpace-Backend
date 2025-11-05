package com.newyork.sharespace.api.dto.workspace

import com.newyork.sharespace.services.entity.Workspace
import java.util.*


data class WorkspaceResponse(
    val id: UUID,
    val title: String,
    val description: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val price: Double,
    val amenities: Set<String>,
    val images: String?,
    val rating: Double?,
    val reviewsNo: Int?,
    val capacity: Int?,
    val availability: Boolean,
    val houseRules: String?
)

fun Workspace.toWorkspaceResponse() = WorkspaceResponse(
    id = this.id,
    title = this.title,
    description = this.description,
    location = this.location,
    latitude = this.latitude,
    longitude = this.longitude,
    price = this.price,
    amenities = this.amenities.map { it.name }.toSet(),
    images = this.images,
    rating = this.rating,
    reviewsNo = this.reviewsNo,
    capacity = this.capacity,
    availability = this.availability,
    houseRules = this.houseRules
)
