package com.newyork.sharespace.services

import com.braintreegateway.*
import com.braintreegateway.exceptions.NotFoundException
import com.newyork.sharespace.api.dto.CardDataResponse
import com.newyork.sharespace.repository.UserRepository
import com.newyork.sharespace.services.entity.User
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class BraintreeService(
    private val braintreeGateway: BraintreeGateway,
    val userRepository: UserRepository
) {

    fun generateClientToken(): String {
        return braintreeGateway.clientToken().generate(ClientTokenRequest())
    }

    fun processPayment(amount: BigDecimal, paymentToken: String): PaymentResult {
        val request = TransactionRequest()
            .amount(amount)
            .paymentMethodToken(paymentToken)
            .options()
            .submitForSettlement(true)
            .done()

        val result: Result<Transaction> = braintreeGateway.transaction().sale(request)

        return if (result.isSuccess) {
            val transaction = result.target
            PaymentResult.Success(transaction.id)
        } else {
            val errorMessage = result.message ?: "Unknown error"
            PaymentResult.Error(errorMessage)
        }
    }

    fun createBrainTreeUserId(user: User): String {
        val userRequest = CustomerRequest().firstName(user.name)
        val result = braintreeGateway.customer().create(userRequest)

        if (result.isSuccess) {
            val updatedUser = user.copy(brainTreeID = result.target.id)
            userRepository.save(updatedUser)
            return result.target.id
        } else {
            throw RuntimeException(result.message ?: "Failed to create Braintree user")
        }
    }

    fun SaveUserCard(userID: UUID, nonce: String) : PaymentResult {
        val user = userRepository.findById(userID).orElse(null)
        val brainTreeID = if (user.brainTreeID != null) {
            user.brainTreeID
        } else {
            createBrainTreeUserId(user)
        }
        val request = PaymentMethodRequest()
            .customerId(brainTreeID).paymentMethodNonce(nonce)
        val result = braintreeGateway.paymentMethod().create(request)

        return if (result.isSuccess) {
            PaymentResult.Success(result.target.token) // Return Success wrapper
        } else {
            PaymentResult.Error(result.message ?: "Unknown error") // Return Error wrapper
        }
    }

    fun getUserCards(userID: UUID): List<CardDataResponse> {
        val user = userRepository.findById(userID)
            .orElseThrow { RuntimeException("User not found") }
        val brainTreeID = user.brainTreeID ?: createBrainTreeUserId(user)
        try {
            val customer = braintreeGateway.customer().find(brainTreeID)
            return customer.paymentMethods.mapNotNull { method ->
                if (method is CreditCard) {
                    CardDataResponse(
                        type = "CreditCard",
                        token = method.token,
                        cardType = method.cardType,
                        last4 = method.last4,
                        imageUrl = method.imageUrl
                    )
                } else {
                    null
                }
            }

        } catch (e: NotFoundException) {
            return emptyList()
        }
    }
}

sealed class PaymentResult {
    data class Success(val transactionId: String) : PaymentResult()
    data class Error(val message: String) : PaymentResult()
}