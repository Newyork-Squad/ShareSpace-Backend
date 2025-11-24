package com.newyork.sharespace.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.newyork.sharespace.repository.NotificationRepository
import com.newyork.sharespace.repository.UserRepository
import com.newyork.sharespace.services.entity.Notification
import com.newyork.sharespace.services.entity.NotificationType
import org.springframework.stereotype.Service
import java.util.*
import com.google.firebase.messaging.Notification as FcmNotification

@Service
class FCMService(
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository
) {

    fun sendNotificationToUser(
        userId: UUID,
        title: String,
        message: String,
        type: NotificationType,
        data: Map<String, String>? = null
    ) {
        val user = userRepository.findById(userId).orElse(null) ?: return

        if (user.fcmToken.isNullOrBlank()) {
            saveNotification(userId, title, message, type)
            return
        }

        try {
            val fcmNotification = FcmNotification.builder()
                .setTitle(title)
                .setBody(message)
                .build()

            val messageBuilder = Message.builder()
                .setToken(user.fcmToken)
                .setNotification(fcmNotification)
                .putData("type", type.name)

            data?.let { messageBuilder.putAllData(it) }

            FirebaseMessaging.getInstance().send(messageBuilder.build())

            saveNotification(userId, title, message, type)

        } catch (e: Exception) {
            e.printStackTrace()
            saveNotification(userId, title, message, type)
        }
    }

    private fun saveNotification(
        userId: UUID,
        title: String,
        message: String,
        type: NotificationType
    ) {
        val notification = Notification(
            userId = userId,
            title = title,
            message = message,
            type = type,
            isRead = false
        )
        notificationRepository.save(notification)
    }

    fun getUserNotifications(userId: UUID): List<Notification> {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
    }

    fun markAsRead(notificationId: UUID) {
        notificationRepository.findById(notificationId).ifPresent { notification ->
            notificationRepository.save(
                notification.copy(isRead = true)
            )
        }
    }

    fun getUnreadCount(userId: UUID): Long {
        return notificationRepository.countByUserIdAndIsReadFalse(userId)
    }
}