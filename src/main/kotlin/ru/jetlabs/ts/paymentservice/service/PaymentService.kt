package ru.jetlabs.ts.paymentservice.service

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.jetlabs.ts.paymentservice.PaymentServiceApplication
import ru.jetlabs.ts.paymentservice.rest.AddAgencyBindingRequest
import ru.jetlabs.ts.paymentservice.tables.AgencyBankAccountsBindings
import java.sql.SQLException

sealed interface AddAgencyBindingResult {
    data object Success : AddAgencyBindingResult
    sealed interface Error : AddAgencyBindingResult {
        val message: String

        data class AgencyAlreadyExist(val agencyId: Long) : Error {
            override val message: String = "Agency already exists(id = $agencyId)"
        }

        data class Unknown(override val message: String) : Error
    }
}

@Component
@Transactional
class PaymentService {
    companion object {
        val LOGGER = LoggerFactory.getLogger(PaymentServiceApplication::class.java)!!
    }

    fun addAgencyBinding(addAgencyBindingRequest: AddAgencyBindingRequest): AddAgencyBindingResult = try {
        AgencyBankAccountsBindings.insert {
            it[agencyId] = addAgencyBindingRequest.agencyId
            it[bankAccountNumber] = addAgencyBindingRequest.bankAccountNumber
        }
        AddAgencyBindingResult.Success
    } catch (e: SQLException) {
        if (AgencyBankAccountsBindings.select(emptyList())
                .where(AgencyBankAccountsBindings.agencyId eq addAgencyBindingRequest.agencyId).count() >= 1
        ) AddAgencyBindingResult.Error.AgencyAlreadyExist(addAgencyBindingRequest.agencyId)
        else AddAgencyBindingResult.Error.Unknown(e.message ?: e.stackTraceToString()).also {
            LOGGER.error(it.toString(), e)
        }
    }
}