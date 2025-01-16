package ru.jetlabs.ts.paymentservice.service

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.upsert
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.jetlabs.ts.paymentservice.PaymentServiceApplication
import ru.jetlabs.ts.paymentservice.config.TicketFeignClient
import ru.jetlabs.ts.paymentservice.models.*
import ru.jetlabs.ts.paymentservice.tables.AgencyBankAccountsBindings
import ru.jetlabs.ts.paymentservice.tables.Transactions
import ru.jetlabs.ts.paymentservice.tables.TransactionsStatuses
import java.sql.SQLException


@Component
@Transactional
class PaymentService (
    val tfc: TicketFeignClient
){
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
                    agencyId = it[AgencyBankAccountsBindings.agencyId],
                    bankAccountNumber = it[AgencyBankAccountsBindings.bankAccountNumber],

                )
            )
        } ?: GetByIdResult.NotFound

    fun addTransaction(uuid: String, agencyId: Long, userId: Long, amount: Double, ticketId: Long) {
        Transactions.insert {
            it[this.uuid] = uuid
            it[this.agencyId] = agencyId
            it[this.userId] = userId
            it[this.ticketId] = ticketId
            it[this.amount] = amount
        }
    }

    fun handleStatus(body: TransactionStatusHookBody): Int {
        val v = Transactions.select(Transactions.id, Transactions.ticketId)
            .where { Transactions.uuid eq body.transactionUuid }
            .singleOrNull()?.let {
                GetTransactionByUuidResult.Success(
                    GetTransactionByUuidData(
                        id = it[Transactions.id].value,
                        ticketId = it[Transactions.ticketId]
                    )
                )
            } ?: GetTransactionByUuidResult.NotFound
        val ticketId = v.let {
            when(it) {
                is GetTransactionByUuidResult.NotFound -> -1
                is GetTransactionByUuidResult.Success -> it.id.ticketId
            }
        }

        if(body.status==AcquiringTransactionStatus.APPROVED && ticketId!=-1L){
            tfc.approveTicket(ticketId)
        }else if(body.status==AcquiringTransactionStatus.EXPIRED && ticketId!=-1L){
            tfc.cancelTicket(ticketId)
        }
        return v.let { t ->
            when(t){
                GetTransactionByUuidResult.NotFound -> -1
                is GetTransactionByUuidResult.Success -> TransactionsStatuses.update(where = {
                    TransactionsStatuses.transaction eq t.id.id
                }){
                    it[status] = body.status
                }
            }
        }
    }
}

sealed interface HandleStatusResult {
    data object Success
    data class UnknownError(val error: String) : HandleStatusResult
}
