/*
    Implements the data access layer (DAL).
    This file implements the database queries used to fetch and insert rows in our database tables.

    See the `mappings` module for the conversions between database rows and Kotlin objects.
 */

package io.pleo.antaeus.data

import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Customer
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class AntaeusDal(private val db: Database) {
    fun fetchInvoice(id: Int): Invoice? {
        // transaction(db) runs the internal query as a new database transaction.
        return transaction(db) {
            // Returns the first invoice with matching id.
            InvoiceTable
                .select { InvoiceTable.id.eq(id) }
                .firstOrNull()
                ?.toInvoice()
        }
    }

    fun fetchInvoices(): List<Invoice> {
        return transaction(db) {
            InvoiceTable
                .selectAll()
                .map { it.toInvoice() }
        }
    }

    fun fetchInvoices(status: InvoiceStatus): List<Invoice> {
        return transaction(db) {
            InvoiceTable
                    .select { InvoiceTable.status.eq(status.name) }
                    .map { it.toInvoice() }
        }
    }

    fun createInvoice(amount: Money, customer: Customer, status: InvoiceStatus = InvoiceStatus.PENDING): Invoice? {
        val id = transaction(db) {
            // Insert the invoice and returns its new id.
            InvoiceTable
                .insert {
                    it[this.value] = amount.value
                    it[this.currency] = amount.currency.toString()
                    it[this.status] = status.toString()
                    it[this.customerId] = customer.id
                } get InvoiceTable.id
        }

        return fetchInvoice(id!!)
    }

    fun updateInvoice(invoice: Invoice) : Invoice? {
        transaction(db) {
            //Update invoice data
            InvoiceTable
                    .update({ InvoiceTable.id.eq(invoice.id) }) {
                        it[this.value] = invoice.amount.value
                        it[this.currency] = invoice.amount.currency.name
                        it[this.status] = invoice.status.name
                        it[this.customerId] = invoice.customerId
                    }
        }

        return fetchInvoice(invoice.id)
    }

    fun fetchCustomer(id: Int): Customer? {
        return transaction(db) {
            CustomerTable
                .select { CustomerTable.id.eq(id) }
                .firstOrNull()
                ?.toCustomer()
        }
    }

    fun fetchCustomers(): List<Customer> {
        return transaction(db) {
            CustomerTable
                .selectAll()
                .map { it.toCustomer() }
        }
    }

    fun createCustomer(currency: Currency): Customer? {
        val id = transaction(db) {
            // Insert the customer and return its new id.
            CustomerTable.insert {
                it[this.currency] = currency.toString()
            } get CustomerTable.id
        }

        return fetchCustomer(id!!)
    }

    // TODO: Check syntax
    // fun batchUpdateInvoices(InvoiceTable, status: InvoiceStatus = InvoiceStatus.PENDING): List<Invoice>{
    //     val billedInvoices = listOf<Invoice>()
    //     BatchUpdateStatement(InvoiceTable).apply {
    //     billedInvoices.forEach {
    //         addBatch(it.id)
    //         this[InvoiceTable.status] = status
    //     }
    //     execute(Transaction.current())
    //     }
    //     // need to return updated invoices
    // }
}
