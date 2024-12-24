package ru.jetlabs.ts.paymentservice.rest

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/ts-payment-service")
class PaymentServiceController {
    @PostMapping("/bind-agency")
    fun addAgencyBinding(
        @RequestBody d: String
    ) {

    }

    @GetMapping
    fun transactionCallback(){

    }
}

data class AddAgencyBindingRequest(
    val agencyId: Long,
    val bankAccountNumber: String,
)