package ru.jetlabs.ts.paymentservice.models

sealed interface GetTransactionByUuidResult {
    data class Success(val id: GetTransactionByUuidData) : GetTransactionByUuidResult
    data object NotFound : GetTransactionByUuidResult
}