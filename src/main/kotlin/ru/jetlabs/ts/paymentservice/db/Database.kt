package ru.jetlabs.ts.paymentservice.db

import org.jetbrains.exposed.sql.SchemaUtils
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.jetlabs.ts.paymentservice.tables.AgencyBankAccountsBindings
import ru.jetlabs.ts.paymentservice.tables.Transactions
import ru.jetlabs.ts.paymentservice.tables.TransactionsStatuses

@Component
@Transactional
class SchemaInitialize : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        SchemaUtils.create(Transactions, TransactionsStatuses, AgencyBankAccountsBindings)
    }
}