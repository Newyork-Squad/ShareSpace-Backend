package com.newyork.sharespace.services

import com.newyork.sharespace.api.dto.booking.BookingRequest
import com.newyork.sharespace.api.dto.booking.BookingResponse
import com.newyork.sharespace.api.dto.booking.toBookingResponse
import com.newyork.sharespace.repository.BookingRepository
import com.newyork.sharespace.repository.WorkspaceRepository
import com.newyork.sharespace.services.entity.Booking
import com.newyork.sharespace.services.entity.BookingStatus
import com.newyork.sharespace.services.entity.PaymentStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@Service
class BookingService(
    private val bookingRepository: BookingRepository,
    private val workspaceRepository: WorkspaceRepository
) {

    @Transactional
    fun createBooking(userId: UUID, request: BookingRequest): BookingResponse {

        val workspace = workspaceRepository.findById(request.workspaceId)
            .orElseThrow { IllegalArgumentException("Workspace not found") }

        val startDateTime = parseDateAndTime(request)
        val endDateTime = startDateTime.plusHours(request.durationHours.toLong())
        val cost = request.durationHours * workspace.price
        val now = LocalDateTime.now()

        val booking = Booking(
            workspace = workspace,
            userId = userId,
            startTime = startDateTime,
            endTime = endDateTime,
            duration = request.durationHours,
            paymentType = request.paymentType,
            cost = cost,
            status = BookingStatus.CONFIRMED,
            paymentStatus = PaymentStatus.PAID,
            createdAt = now,
            updatedAt = now
        )

        return bookingRepository.save(booking).toBookingResponse()
    }

    fun cancelBooking(bookingId: UUID, userId: UUID): BookingResponse {

        val booking = bookingRepository.findById(bookingId)
            .orElseThrow { IllegalArgumentException("Booking not found") }

        if (booking.userId != userId) throw IllegalAccessException("Not authorized to cancel this booking")

        val updatedBooking = booking.copy(
            status = BookingStatus.CANCELLED,
            updatedAt = LocalDateTime.now()
        )

        return bookingRepository.save(updatedBooking).toBookingResponse()
    }

    fun getBookingHistory(userId: UUID, pageable: Pageable, status: String? = null): Page<BookingResponse> {

        val bookings = status
            ?.takeIf { it.isNotBlank() && !it.equals(ALL, true) }
            ?.let {
                val bookingStatus = try {
                    BookingStatus.valueOf(it.uppercase())
                } catch (e: IllegalArgumentException) {
                    throw IllegalArgumentException("Invalid booking status: $it")
                }
                bookingRepository.findAllByUserIdAndStatus(userId, bookingStatus, pageable)
            } ?: bookingRepository.findAllByUserId(userId, pageable)

        return bookings.map { it.toBookingResponse() }
    }


    fun getActiveBookings(userId: UUID, pageable: Pageable): Page<BookingResponse> =
        bookingRepository.findAllByUserIdAndStatus(userId, BookingStatus.CONFIRMED, pageable)
            .map { it.toBookingResponse() }

    fun getUpcomingBookings(userId: UUID, pageable: Pageable): Page<BookingResponse> {

        val now = LocalDateTime.now()

        val bookings = bookingRepository.findAllByUserIdAndStatusAndStartTimeAfter(
            userId = userId,
            status = BookingStatus.CONFIRMED,
            startTime = now,
            pageable = pageable
        )

        return bookings.map { it.toBookingResponse() }
    }

    private fun parseDateAndTime(request: BookingRequest): LocalDateTime {

        val bookingDate = LocalDate.parse(request.date)
        val bookingStartTime = LocalTime.parse(request.startTime)
        val startDateTime = LocalDateTime.of(bookingDate, bookingStartTime)

        return startDateTime
    }

    private companion object {
        const val ALL = "ALL"
    }
}
