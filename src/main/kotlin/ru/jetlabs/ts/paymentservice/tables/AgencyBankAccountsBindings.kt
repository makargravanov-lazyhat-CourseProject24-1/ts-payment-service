package ru.jetlabs.ts.paymentservice.tables

import org.jetbrains.exposed.dao.id.IdTable

object AgencyBankAccountsBindings : IdTable<Long>("agency_bank_accounts_bindings") {
    val agencyId = long("agency_id").entityId().uniqueIndex()
    val bankAccountNumber = char("bank_account_number", 16).check { it regexp "^[0-9]{16}$" }
    override val id = Transactions.long("id").entityId().uniqueIndex()
    override val primaryKey = PrimaryKey(id)
}