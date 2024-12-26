package ru.jetlabs.ts.paymentservice.models

sealed interface GetByIdResult {
    data class Success(val agencyBindRequest: AgencyBindRequest) : GetByIdResult
    data object NotFound : GetByIdResult
}