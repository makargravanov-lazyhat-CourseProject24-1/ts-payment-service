package ru.jetlabs.ts.paymentservice.service

import org.jetbrains.exposed.sql.upsert
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.jetlabs.ts.paymentservice.PaymentServiceApplication
import ru.jetlabs.ts.paymentservice.models.AgencyBindResult
import ru.jetlabs.ts.paymentservice.rest.AddAgencyBindingRequest
import ru.jetlabs.ts.paymentservice.tables.AgencyBankAccountsBindings
import java.sql.SQLException


@Component
@Transactional
class PaymentService {
    companion object {
        val LOGGER = LoggerFactory.getLogger(PaymentServiceApplication::class.java)!!
    }

    fun bindAgency(addAgencyBindingRequest: AddAgencyBindingRequest): AgencyBindResult = try {
        AgencyBankAccountsBindings.upsert(where = {
            AgencyBankAccountsBindings.agencyId eq addAgencyBindingRequest.agencyId
        }) {
            it[agencyId] = addAgencyBindingRequest.agencyId
            it[bankAccountNumber] = addAgencyBindingRequest.bankAccountNumber
        }
        AgencyBindResult.Success
    } catch (e: SQLException) {
        AgencyBindResult.UnknownError(e.message ?: e.stackTraceToString()).also {
            LOGGER.error(it.toString(), e)
        }
    }
}