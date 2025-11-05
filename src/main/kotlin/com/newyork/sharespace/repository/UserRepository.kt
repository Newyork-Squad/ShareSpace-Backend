package com.newyork.sharespace.repository

import com.newyork.sharespace.services.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByPhoneNumber(email: String): User?
    fun findByEmail(email: String): User?
}