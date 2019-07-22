package io.pleo.antaeus.models

data class Transaction(
    val customerID: Int,
    val invoice: Invoice,
    val accepted: Boolean,
    val description: String,
    val status: BillStatus
)