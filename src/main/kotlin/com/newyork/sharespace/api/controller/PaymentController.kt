package com.newyork.sharespace.api.controller
import com.newyork.sharespace.api.dto.CardDataResponse
import com.newyork.sharespace.api.dto.CheckoutRequest
import com.newyork.sharespace.api.dto.SaveCardRequest
import com.newyork.sharespace.config.JwtAuthFilter
import com.newyork.sharespace.services.BraintreeService
import com.newyork.sharespace.services.PaymentResult
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payments")
class PaymentController(
    private val braintreeService: BraintreeService
) {
    @GetMapping("/token")
    fun getClientToken(): ResponseEntity<Map<String, String>> {
        val token = braintreeService.generateClientToken()
        return ResponseEntity.ok(mapOf("token" to token))
    }
    @PostMapping("/cards")
    fun saveCard(@RequestBody request: SaveCardRequest): ResponseEntity<Any> {
        val userId = JwtAuthFilter.getUserId()
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val resultString = braintreeService.SaveUserCard(userId, request.nonce)
        return ResponseEntity.ok(mapOf("result" to resultString))
    }
    @GetMapping("/cards")
    fun getUserCards(): ResponseEntity<List<CardDataResponse>> {
        val userId = JwtAuthFilter.getUserId()
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val cards = braintreeService.getUserCards(userId)
        return ResponseEntity.ok(cards)
    }
    @PostMapping("/checkout")
    fun checkout(@RequestBody request: CheckoutRequest): ResponseEntity<Any> {
        val result = braintreeService.processPayment(request.amount, request.paymentToken)
        return when (result) {
            is PaymentResult.Success -> ResponseEntity.ok(
                mapOf(
                    "success" to true,
                    "transactionId" to result.transactionId
                )
            )
            is PaymentResult.Error -> ResponseEntity.badRequest().body(
                mapOf(
                    "success" to false,
                    "message" to result.message
                )
            )
        }
    }
}



