package io.pleo.antaeus.core.exceptions

import io.pleo.antaeus.models.Currency

class CurrencyInvalidException(currency: Currency) : 
    Exception("Currency Symbol '$currency' is invalid")