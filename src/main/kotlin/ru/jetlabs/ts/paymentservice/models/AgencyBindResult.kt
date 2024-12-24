package ru.jetlabs.ts.paymentservice.models

sealed interface AgencyBindResult {
    data object Success : AgencyBindResult
    data class UnknownError(val message: String) : AgencyBindResult
}