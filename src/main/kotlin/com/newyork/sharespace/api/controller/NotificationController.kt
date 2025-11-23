package com.newyork.sharespace.api.controller

import com.newyork.sharespace.config.JwtAuthFilter
import com.newyork.sharespace.config.exceptionHandling.ApiResponse
import com.newyork.sharespace.config.exceptionHandling.ResourceNotFoundException
import com.newyork.sharespace.config.exceptionHandling.UnauthorizedException
import com.newyork.sharespace.services.FCMService
import com.newyork.sharespace.services.entity.Notification
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val fcmService: FCMService
) {

    @GetMapping
    fun getNotifications(): ResponseEntity<ApiResponse<List<Notification>>> {
        val notifications = fcmService.getUserNotifications(getCurrentUserId())
        return ResponseEntity.ok(ApiResponse.success(notifications, "Notifications retrieved successfully"))
    }

    @GetMapping("/unread-count")
    fun getUnreadCount(): ResponseEntity<ApiResponse<Map<String, Long>>> {
        val count = fcmService.getUnreadCount(getCurrentUserId())
        return ResponseEntity.ok(ApiResponse.success(mapOf("unreadCount" to count)))
    }

    @PutMapping("/{id}/read")
    fun markAsRead(@PathVariable id: UUID): ResponseEntity<ApiResponse<Nothing>> {
        val userId = getCurrentUserId()

        if (fcmService.getUserNotifications(userId).none { it.id == id }) {
            throw ResourceNotFoundException("Notification not found")
        }

        fcmService.markAsRead(id)
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read"))
    }

    @PutMapping("/mark-all-read")
    fun markAllAsRead(): ResponseEntity<ApiResponse<Nothing>> {
        fcmService.getUserNotifications(getCurrentUserId())
            .forEach { fcmService.markAsRead(it.id) }
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read"))
    }

    private fun getCurrentUserId(): UUID =
        JwtAuthFilter.getUserId() ?: throw UnauthorizedException("User not authenticated")
}