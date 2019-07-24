package io.pleo.antaeus.models

enum class TransactionStatus {
    AUTHORIZED,
    GATEWAY_REJECTED,
    FAILED,
    PROCESSOR_DECLINED,
    CURRENCY_MISMATCH,
    INSUFFICIENT_FUNDS,
    CUSTOMER_NOT_FOUND,
    NETWORK_ERROR
}