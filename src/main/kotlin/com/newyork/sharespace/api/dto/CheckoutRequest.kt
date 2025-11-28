package com.newyork.sharespace.api.dto

import java.math.BigDecimal


data class CheckoutRequest(
    val amount: BigDecimal,
    val paymentToken: String // Can be a nonce (new card) or a token (saved card)
)