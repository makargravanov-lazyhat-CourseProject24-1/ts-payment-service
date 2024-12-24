package ru.jetlabs.ts.paymentservice.models

enum class AcquiringTransactionStatus {
    CREATED,
    APPROVED,
    EXPIRED,
    ROLLBACK,
}