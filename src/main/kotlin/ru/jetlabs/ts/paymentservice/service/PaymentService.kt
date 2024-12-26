package ru.jetlabs.ts.paymentservice.service

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.jetlabs.ts.paymentservice.PaymentServiceApplication
import ru.jetlabs.ts.paymentservice.models.AgencyBindRequest
import ru.jetlabs.ts.paymentservice.models.AgencyBindResult
import ru.jetlabs.ts.paymentservice.models.GetByIdResult
import ru.jetlabs.ts.paymentservice.models.TransactionStatusHookBody
import ru.jetlabs.ts.paymentservice.tables.AgencyBankAccountsBindings
import ru.jetlabs.ts.paymentservice.tables.Transactions
import ru.jetlabs.ts.paymentservice.tables.TransactionsStatuses
import java.sql.SQLException


@Component
@Transactional
class PaymentService {
    companion object {
        val LOGGER = LoggerFactory.getLogger(PaymentServiceApplication::class.java)!!
    }

    fun bindAgency(addAgencyBindingRequest: AgencyBindRequest): AgencyBindResult = try {
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

    fun getById(id: Long): GetByIdResult =
        AgencyBankAccountsBindings.selectAll().where { AgencyBankAccountsBindings.agencyId eq id }.singleOrNull()?.let {
            GetByIdResult.Success(
                AgencyBindRequest(
                    agencyId = it[AgencyBankAccountsBindings.agencyId].value,
                    bankAccountNumber = it[AgencyBankAccountsBindings.bankAccountNumber],

                )
            )
        } ?: GetByIdResult.NotFound

    fun addTransaction(uuid: String, agencyId: Long, userId: Long, amount: Float) {
        Transactions.insert {
            it[this.uuid] = uuid
            it[this.agencyId] = agencyId
            it[this.userId] = userId
            it[this.amount] = amount
        }
    }

    fun handleStatus(body: TransactionStatusHookBody): HandleStatusResult {

    }
}

sealed interface HandleStatusResult {
    data object Success
    data class UnknownError(val error: String) : HandleStatusResult
}
