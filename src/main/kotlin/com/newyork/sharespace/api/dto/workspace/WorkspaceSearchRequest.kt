package com.newyork.sharespace.api.dto.workspace

data class WorkspaceSearchRequest(
    val keyword: String? = null,
    val location: String? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val minRating: Double? = null,
    val services: Set<String>? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
