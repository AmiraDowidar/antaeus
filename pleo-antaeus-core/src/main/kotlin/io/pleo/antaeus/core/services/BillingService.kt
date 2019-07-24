package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Transaction
import io.pleo.antaeus.models.TransactionStatus
import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.exceptions.NetworkException

class BillingService(
    private val paymentProvider: PaymentProvider
) {
   // TODO - Add code e.g. here

    private fun bill(invoice: Invoice): Transaction {
        try {
            val successful = paymentProvider.charge(invoice)
            if (successful) {
                return Transaction(invoice.copy(status = InvoiceStatus.PAID), true, "Success", TransactionStatus.AUTHORIZED)
            } else {
                return Transaction(invoice, false, "Insufficient funds", TransactionStatus.INSUFFICIENT_FUNDS)
            }
        }
        catch (e: Exception) {
            val status = when (e) {
                is CustomerNotFoundException -> TransactionStatus.CUSTOMER_NOT_FOUND
                is CurrencyMismatchException -> TransactionStatus.CURRENCY_MISMATCH
                is NetworkException -> TransactionStatus.NETWORK_ERROR
                else -> TransactionStatus.FAILED
            }
            return Transaction(invoice, false, e.toString(), status)
        }
    }
}