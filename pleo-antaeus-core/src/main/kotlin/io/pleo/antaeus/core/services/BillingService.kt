package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Transaction
import io.pleo.antaeus.models.TransactionStatus

class BillingService(
    private val paymentProvider: PaymentProvider
) {
   // TODO - Add code e.g. here

    private fun bill(invoice: Invoice): Transaction {
        try {
            val paid = paymentProvider.charge(invoice)
            if (paid) {
                return Transaction(invoice.copy(status = InvoiceStatus.PAID), true, "Success", TransactionStatus.AUTHORIZED)
            } else {
                return Transaction(invoice, false, "Insufficient funds", TransactionStatus.INSUFFICIENT_FUNDS)
            }
        }
        catch (e: Exception) {
            return Transaction(invoice, false, e.toString(), TransactionStatus.FAILED)
        }
    }
}