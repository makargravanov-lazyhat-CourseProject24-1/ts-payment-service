package ru.jetlabs.ts.paymentservice.tables

import org.jetbrains.exposed.dao.id.LongIdTable

object AgencyBankAccountsBindings : LongIdTable("agency_bank_accounts_bindings") {
    val agencyId = long("agency_id").uniqueIndex()
    val bankAccountNumber = char("bank_account_number", 16).check { it regexp "^[0-9]{16}$" }
}