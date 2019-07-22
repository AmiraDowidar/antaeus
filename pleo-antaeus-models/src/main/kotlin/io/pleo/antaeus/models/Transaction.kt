package io.pleo.antaeus.models

data class Transaction(
    val invoice: Invoice,
    val accepted: Boolean,
    val description: String,
    val status: TransactionStatus
)