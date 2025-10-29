package com.newyork.sharespace.services.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Email
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false,unique = true)
    @Email
    val email: String,
    @Column(nullable = false)
    val password: String,
    @Column(nullable = false, unique = true)
    val phoneNumber: String,
    @Column(nullable = false)
    val gender: Gender,
    @Column(name = "image_url", nullable = true, length = 2083)
    val imageUrl: String?,
    @Column(nullable = true)
    val bio: String?,
)
enum class Gender {
    MALE, FEMALE
}
