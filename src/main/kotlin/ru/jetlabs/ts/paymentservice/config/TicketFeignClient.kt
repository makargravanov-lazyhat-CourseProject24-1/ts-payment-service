package ru.jetlabs.ts.paymentservice.config

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping

@FeignClient(name = "ts-agency-service", url = "http://ts-agency-service:8080/ts-agency-service/api/v1")
interface TicketFeignClient {
    @PutMapping("/{id}/cancel")
    fun cancelTicket(@PathVariable id: Long): ResponseEntity<*>
    @PutMapping("/{id}/approve")
    fun approveTicket(@PathVariable id: Long): ResponseEntity<*>
}