package ru.jetlabs.ts.paymentservice.models

data class AgencyBindRequest(
    val agencyId: Long,
    val bankAccountNumber: String,
)
