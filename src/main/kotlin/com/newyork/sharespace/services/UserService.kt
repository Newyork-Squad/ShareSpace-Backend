package com.newyork.sharespace.services

import com.newyork.sharespace.repository.UserRepository
import com.newyork.sharespace.services.entity.User
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {

    fun getUserById(id: UUID): User? = userRepository.findById(id).orElse(null)

    fun getAllUsers(): List<User> = userRepository.findAll()
}