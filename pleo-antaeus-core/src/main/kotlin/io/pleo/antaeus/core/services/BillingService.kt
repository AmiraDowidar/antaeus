package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.external.PaymentProvider

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
                return Transaction(invoice, false, "Insufficient funds", TransactionStatus.FAILED)
            }
        }
        catch (e: Exception) {
            return Transaction(invoice, false, e.toString(), TransactionStatus.FAILED)
        }
    }
}