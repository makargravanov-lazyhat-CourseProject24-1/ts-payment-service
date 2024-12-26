package ru.jetlabs.ts.paymentservice.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime



object Transactions : IdTable<Long>("transactions") {
    override val id = long("id").entityId().uniqueIndex()
    override val primaryKey = PrimaryKey(id)
    val uuid = text("uuid")
    val userId = long("user_id")
    val amount = float("amount")
    val agencyId = long("agency_id")
    val ticketId = long("ticketId")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
}