package ru.jetlabs.ts.paymentservice.models

data class TransactionStatusHookBody(val bankAccountId: String, val status: AcquiringTransactionStatus)