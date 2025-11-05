package com.newyork.sharespace.api.dto

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)