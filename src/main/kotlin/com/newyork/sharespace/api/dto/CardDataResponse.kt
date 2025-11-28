package com.newyork.sharespace.api.dto

data class CardDataResponse(
    val type: String,
    val token: String,
    val cardType: String,
    val last4: String,
    val imageUrl: String
)
