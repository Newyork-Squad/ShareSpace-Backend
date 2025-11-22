package com.newyork.sharespace.api.controller

import com.newyork.sharespace.api.dto.ChangePasswordRequest
import com.newyork.sharespace.api.dto.UserResponse
import com.newyork.sharespace.api.dto.UserUpdateRequest
import com.newyork.sharespace.api.dto.toUserResponse
import com.newyork.sharespace.config.JwtAuthFilter
import com.newyork.sharespace.config.exceptionHandling.ApiResponse
import com.newyork.sharespace.repository.UserRepository
import com.newyork.sharespace.services.AuthService
import com.newyork.sharespace.services.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = ["*"])
class UserController(
    private val userService: UserService,
    private val authService: AuthService,
    private val userRepository: UserRepository
) {


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

    @PostMapping(
        path = ["/me/image"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun updateUserImage(
        @RequestPart("file") file: MultipartFile,
    ): ResponseEntity<ApiResponse<String>> {

        if (file.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "File must not be empty")
        }
        val userId = JwtAuthFilter.getUserId()
            ?: return ResponseEntity.status(404).body(ApiResponse.error("User not found"))

        val user = userService.getUserById(userId)
            ?: return ResponseEntity.status(404).body(ApiResponse.error("User not found"))
        val newImageUrl = userService.uploadImage(file, user.phoneNumber)
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image")

        val updatedUser = user.copy(imageUrl = newImageUrl)
        userRepository.save(updatedUser)
        val response = ApiResponse.success(
            data = newImageUrl,
            message = "Image updated successfully"
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping("/me/update")
    fun updateUser(
        @RequestBody userUpdateRequest: UserUpdateRequest
    ): ResponseEntity<ApiResponse<UserResponse>> {

        val userId = JwtAuthFilter.getUserId()
            ?: return ResponseEntity.status(404)
                .body(ApiResponse.error("User not found"))

        val updated = userService.updateUserInfo(userId, userUpdateRequest)
            ?: return ResponseEntity.status(404)
                .body(ApiResponse.error("User not found"))

        return ResponseEntity.ok(
            ApiResponse.success(
                updated.toUserResponse(),
                "User updated successfully"
            )
        )
    }

    @PostMapping("/change-password")
    fun changePassword(
        @Valid @RequestBody body: ChangePasswordRequest
    ): ResponseEntity<ApiResponse<String>> {

        val userId = JwtAuthFilter.getUserId()
            ?: return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"))

        authService.changePassword(userId, body)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = "Password updated successfully",
                message = "Password updated successfully"
            )
        )
    }


}
