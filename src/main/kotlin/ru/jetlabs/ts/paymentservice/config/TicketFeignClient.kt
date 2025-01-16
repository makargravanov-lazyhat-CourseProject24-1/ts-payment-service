package ru.jetlabs.ts.paymentservice.config

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping

@FeignClient(name = "ts-tickets-service", url = "http://ts-tickets-service:8080/ts-tickets-service/api/v1/tickets")
interface TicketFeignClient {
    @PutMapping("/{id}/cancel")
    fun cancelTicket(@PathVariable id: Long): ResponseEntity<*>
    @PutMapping("/{id}/approve")
    fun approveTicket(@PathVariable id: Long): ResponseEntity<*>
}