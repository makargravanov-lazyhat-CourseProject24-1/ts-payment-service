package ru.jetlabs.ts.paymentservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PaymentServiceApplication

fun main(args: Array<String>): Unit {
    runApplication<PaymentServiceApplication>(*args)
}