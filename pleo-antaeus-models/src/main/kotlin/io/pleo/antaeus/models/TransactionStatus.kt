package io.pleo.antaeus.models

enum class TransactionStatus {
    AUTHORIZED,
    GATEWAY_REJECTED,
    FAILED,
    PROCESSOR_DECLINED,
}