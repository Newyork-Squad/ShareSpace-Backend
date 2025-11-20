package com.newyork.sharespace.api.dto

import com.newyork.sharespace.services.entity.Gender

data class UserUpdateRequest(
    val name: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val gender: Gender? = null,
    val bio: String? = null,
    val imageUrl: String? = null
)
