package ru.jetlabs.ts.paymentservice.models

data class TransactionStatusHookBody(
    val transactionUuid: String,
    val status: AcquiringTransactionStatus
)