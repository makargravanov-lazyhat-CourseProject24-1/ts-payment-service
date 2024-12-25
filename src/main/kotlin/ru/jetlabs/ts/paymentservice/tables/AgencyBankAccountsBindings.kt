package ru.jetlabs.ts.paymentservice.tables

import org.jetbrains.exposed.sql.Table

object AgencyBankAccountsBindings : Table("agency_bank_accounts_bindings") {
    val agencyId = long("agency_id").entityId().uniqueIndex()
    val bankAccountNumber = char("bank_account_number", 16).check { it regexp "^[0-9]{16}$" }

    override val primaryKey = PrimaryKey(agencyId)
}