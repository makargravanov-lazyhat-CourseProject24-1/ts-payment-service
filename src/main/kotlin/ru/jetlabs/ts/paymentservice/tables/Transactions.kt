package ru.jetlabs.ts.paymentservice.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime


object Transactions : LongIdTable("transactions") {
    val uuid = text("uuid")
    val userId = long("user_id")
    val amount = float("amount")
    val agencyId = long("agency_id")
    val ticketId = long("ticket_id")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
}