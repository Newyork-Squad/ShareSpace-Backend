package com.newyork.sharespace.services

import com.newyork.sharespace.api.dto.ImageUploadResponse
import com.newyork.sharespace.api.dto.UserUpdateRequest
import com.newyork.sharespace.repository.UserRepository
import com.newyork.sharespace.services.entity.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val restTemplate: RestTemplate,
    @Value("\${sharespace.image-repo.url}")
    private val uploadUrl: String
) {

    fun getUserById(id: UUID): User? = userRepository.findById(id).orElse(null)

    fun getAllUsers(): List<User> = userRepository.findAll()

    fun uploadImage(file: MultipartFile, userIdentifier: String): String? {
        if (file.isEmpty) return null

        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val fileResource = object : ByteArrayResource(file.bytes) {
            override fun getFilename(): String {
                return file.originalFilename ?: "file.jpg"
            }
        }

        val body = LinkedMultiValueMap<String, Any>()
        body.add("file", fileResource)

        val requestEntity = HttpEntity(body, headers)
        val url = "$uploadUrl/$userIdentifier"

        return try {
            val response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                ImageUploadResponse::class.java // Deserialize response directly
            )
            val imageUrl = response.body?.link
            imageUrl
        } catch (ex: RestClientException) {
            null
        }

    }

    fun updateUserInfo(userId: UUID, userUpdateRequest: UserUpdateRequest): User? {
        val user = userRepository.findById(userId).orElse(null) ?: return null

        val updatedUser = user.copy(
            name = userUpdateRequest.name ?: user.name,
            email = userUpdateRequest.email ?: user.email,
            phoneNumber = userUpdateRequest.phoneNumber ?: user.phoneNumber,
            gender = userUpdateRequest.gender ?: user.gender,
            bio = userUpdateRequest.bio ?: user.bio,
            imageUrl = userUpdateRequest.imageUrl ?: user.imageUrl
        )

        return userRepository.save(updatedUser)
    }


}