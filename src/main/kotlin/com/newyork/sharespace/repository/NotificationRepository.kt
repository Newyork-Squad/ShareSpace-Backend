package com.newyork.sharespace.repository

import com.newyork.sharespace.services.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NotificationRepository : JpaRepository<Notification, UUID> {
    fun findByUserIdOrderByCreatedAtDesc(userId: UUID): List<Notification>
    fun countByUserIdAndIsReadFalse(userId: UUID): Long
}