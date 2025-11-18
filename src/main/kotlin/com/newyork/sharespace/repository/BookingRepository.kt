package com.newyork.sharespace.repository

import com.newyork.sharespace.services.entity.Booking
import com.newyork.sharespace.services.entity.BookingStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface BookingRepository : JpaRepository<Booking, UUID> {

    fun findAllByUserId(userId: UUID, pageable: Pageable): Page<Booking>
    fun findAllByUserIdAndStatus(userId: UUID, status: BookingStatus, pageable: Pageable): Page<Booking>
    fun findAllByUserIdAndStatusAndStartTimeAfter(
        userId: UUID,
        status: BookingStatus,
        startTime: LocalDateTime,
        pageable: Pageable
    ): Page<Booking>
}