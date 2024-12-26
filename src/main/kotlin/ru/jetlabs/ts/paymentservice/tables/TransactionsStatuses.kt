package ru.jetlabs.ts.paymentservice.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import ru.jetlabs.ts.paymentservice.models.AcquiringTransactionStatus
import ru.jetlabs.ts.paymentservice.models.TransactionStatus
import java.time.LocalDateTime

object TransactionsStatuses : LongIdTable("transactions_statuses") {
    val transaction = reference("transaction_id", Transactions)
    val status = enumeration<AcquiringTransactionStatus>("status")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
}