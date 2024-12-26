package ru.jetlabs.ts.paymentservice.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import ru.jetlabs.ts.paymentservice.models.AgencyBindRequest
import ru.jetlabs.ts.paymentservice.models.AgencyBindResult
import ru.jetlabs.ts.paymentservice.models.GetByIdResult
import ru.jetlabs.ts.paymentservice.models.TransactionStatusHookBody
import ru.jetlabs.ts.paymentservice.service.PaymentService

@RestController
@RequestMapping("/ts-payment-service/api/v1")
class PaymentServiceController(
    val paymentService: PaymentService,
    val restTemplate: RestTemplate
) {
    @PostMapping("/bind-agency")
    fun addAgencyBinding(
        @RequestBody request: AgencyBindRequest
    ): ResponseEntity<*> = paymentService.bindAgency(request).let {
        when (it) {
            is AgencyBindResult.Success -> ResponseEntity.status(HttpStatus.OK).build()
            is AgencyBindResult.UnknownError -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it.message)
        }
    }

    @PostMapping("/bind-transaction-status")
    fun registerTransaction(@RequestBody form: TransactionRegisterRequestForm): ResponseEntity<*> {
        val agencyBankAccount = paymentService.getById(form.agencyId).let {
            when (it) {
                GetByIdResult.NotFound -> ""
                is GetByIdResult.Success -> it.agencyBindRequest.bankAccountNumber
            }
        }
        if (agencyBankAccount == "") return ResponseEntity.badRequest().body(null)
        val v = restTemplate.postForEntity(
            "https://acquiring.lazyhat.ru/acquiring-mock-backend/api/v1/register-pay-processing/amount=${form.amount}/to=${agencyBankAccount}",
            "http://ts-payment-service:8080/ts-payment-service/api/v1/transaction-status-hook",
            String::class.java
        )
        v.body?.let { paymentService.addTransaction(it, form.agencyId, form.userId, form.amount, form.ticketId) }
        val b = v.body
        return ResponseEntity.ok("https://pay.lazyhat.ru/pay/$b")
    }

    @PostMapping("/transaction-status-hook")
    fun transactionStatusHook(@RequestBody body: TransactionStatusHookBody): ResponseEntity<*> {
        paymentService.handleStatus(body)
        return ResponseEntity.ok("Success")
    }
}

data class TransactionRegisterRequestForm(
    val amount: Float,
    val agencyId: Long,
    val ticketId: Long,
    val userId: Long
)


