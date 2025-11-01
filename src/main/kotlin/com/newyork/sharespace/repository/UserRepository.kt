package com.newyork.sharespace.repository

import com.newyork.sharespace.services.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
    fun findByPhone(phone : String) : User?
}