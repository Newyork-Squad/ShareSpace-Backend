package com.newyork.sharespace.api.dto

import com.newyork.sharespace.services.entity.Gender
import com.newyork.sharespace.services.entity.User
import java.util.*

data class UserResponse(
    val id: UUID,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val gender: Gender,
    val imageUrl: String?,
    val bio: String?
)

fun User.toUserResponse() = UserResponse(
    id = this.id,
    name = this.name,
    email = this.email,
    phoneNumber = this.phoneNumber,
    gender = this.gender,
    imageUrl = this.imageUrl,
    bio = this.bio
)
