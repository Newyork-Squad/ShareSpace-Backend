package com.newyork.sharespace.config

import com.braintreegateway.BraintreeGateway
import com.braintreegateway.Environment
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BraintreeConfig {
    @Bean
    fun braintreeGateway(
        @Value("\${braintree.environment}") environment: String,
        @Value("\${paypal.merchantID}") merchantId: String,
        @Value("\${paypal.publicKey}") publicKey: String,
        @Value("\${paypal.privateKey}") privateKey: String
    ): BraintreeGateway {
        val braintreeEnvironment = if (environment.equals("production", ignoreCase = true)) {
            Environment.PRODUCTION
        } else {
            Environment.SANDBOX
        }

        return BraintreeGateway(
            braintreeEnvironment,
            merchantId,
            publicKey,
            privateKey
        )
    }
}