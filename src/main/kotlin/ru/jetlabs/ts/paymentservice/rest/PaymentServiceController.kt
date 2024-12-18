package ru.jetlabs.ts.paymentservice.rest

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ts-payment-service")
class PaymentServiceController {
    @PostMapping("/agency")
    fun addAgencyBinding(
        @RequestBody d: String
    ) {

    }
}

data class AddAgencyBindingRequest(
    val agencyId: Long,
    val bankAccountNumber: String,
)