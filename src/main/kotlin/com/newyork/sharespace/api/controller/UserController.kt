package com.newyork.sharespace.api.controller

import com.newyork.sharespace.api.dto.UserResponse
import com.newyork.sharespace.api.dto.toUserResponse
import com.newyork.sharespace.config.JwtAuthFilter
import com.newyork.sharespace.config.exceptionHandling.ApiResponse
import com.newyork.sharespace.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = ["*"])
class UserController(private val userService: UserService) {


    @GetMapping("/me")
    fun getCurrentUser(): ResponseEntity<ApiResponse<UserResponse>> {
        val userId = JwtAuthFilter.getUserId()
            ?: return ResponseEntity.status(404).body(ApiResponse.error("User not found"))

        val user = userService.getUserById(userId)
            ?: return ResponseEntity.status(404).body(ApiResponse.error("User not found"))

        return ResponseEntity.ok(ApiResponse.success(user.toUserResponse(), "Current user retrieved"))
    }


    @GetMapping("/all")
    fun getAllUsers(): ResponseEntity<ApiResponse<List<UserResponse>>> {
        val users = userService.getAllUsers().map { it.toUserResponse() }
        return ResponseEntity.ok(ApiResponse.success(users, "All users retrieved"))
    }
}