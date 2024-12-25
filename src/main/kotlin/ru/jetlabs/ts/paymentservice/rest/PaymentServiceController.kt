package ru.jetlabs.ts.paymentservice.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.jetlabs.ts.paymentservice.models.AgencyBindRequest
import ru.jetlabs.ts.paymentservice.models.AgencyBindResult
import ru.jetlabs.ts.paymentservice.models.TransactionStatusHookBody
import ru.jetlabs.ts.paymentservice.service.PaymentService

@RestController
@RequestMapping("/ts-payment-service/api/v1")
class PaymentServiceController(
    val paymentService: PaymentService
) {
    @PostMapping("/bind-agency")
    fun addAgencyBinding(
        @RequestBody request: AgencyBindRequest
    ): ResponseEntity<*> = paymentService.bindAgency(request).let {
        when (it) {
            AgencyBindResult.Success -> ResponseEntity.status(HttpStatus.OK).build()
            is AgencyBindResult.UnknownError -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it.message)
        }
    }

    @PostMapping("/bind-transaction-status")
    fun registerTransaction()

    @PostMapping("/transaction-status-hook")
    fun transactionStatusHook(@RequestBody body: TransactionStatusHookBody): ResponseEntity<*> {

    }
}

data class TransactionRegisterRequestForm(
    val bookingId
)


