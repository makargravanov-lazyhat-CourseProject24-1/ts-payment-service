package ru.jetlabs.ts.paymentservice.models

enum class TransactionStatus {
    CREATED,
    IN_PROGRESS,
    SUCCESS,
    ABORTED
}