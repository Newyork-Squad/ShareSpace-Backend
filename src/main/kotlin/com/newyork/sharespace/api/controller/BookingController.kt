package com.newyork.sharespace.api.controller

import com.newyork.sharespace.api.dto.booking.BookingRequest
import com.newyork.sharespace.api.dto.booking.BookingResponse
import com.newyork.sharespace.config.JwtAuthFilter
import com.newyork.sharespace.config.exceptionHandling.ApiResponse
import com.newyork.sharespace.services.BookingService
import com.newyork.sharespace.services.util.buildPageable
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingService: BookingService
) {

    @PostMapping("/book")
    fun bookWorkspace(@RequestBody request: BookingRequest): ResponseEntity<ApiResponse<BookingResponse>> {
        val booking = bookingService.createBooking(getUserId(), request)
        return okResponse(booking, "Booking created successfully")
    }

    @PutMapping("/{bookingId}/cancel")
    fun cancelBooking(@PathVariable bookingId: UUID): ResponseEntity<ApiResponse<BookingResponse>> {
        val canceledBooking = bookingService.cancelBooking(bookingId, getUserId())
        return okResponse(canceledBooking, "Booking canceled successfully")
    }

    @GetMapping("/history")
    fun getBookingHistory(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) status: String? = null
    ): ResponseEntity<ApiResponse<Page<BookingResponse>>> {
        val pageable = buildPageable(page, size)
        val response = bookingService.getBookingHistory(getUserId(), pageable, status)
        return okResponse(response)
    }

    @GetMapping("/active")
    fun getActiveBookings(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<ApiResponse<Page<BookingResponse>>> {
        val pageable = buildPageable(page, size)
        val response = bookingService.getActiveBookings(getUserId(), pageable)
        return okResponse(response)
    }

    @GetMapping("/upcoming")
    fun getUpcomingBookings(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ApiResponse<Page<BookingResponse>>> {
        val pageable = buildPageable(page, size)
        val response = bookingService.getUpcomingBookings(getUserId(), pageable)
        return okResponse(response)
    }


    private fun getUserId(): UUID {
        return JwtAuthFilter.getUserId() ?: throw IllegalStateException("User not authenticated")
    }

    private fun <T> okResponse(data: T, message: String? = null): ResponseEntity<ApiResponse<T>> {
        return ResponseEntity.ok(ApiResponse.success(data, message.toString()))
    }

}
