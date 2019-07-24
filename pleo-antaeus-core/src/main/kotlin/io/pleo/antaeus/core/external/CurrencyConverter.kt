package io.pleo.antaeus.core

import com.beust.klaxon.JsonObject
import io.pleo.antaeus.models.Currency
import java.math.BigDecimal
import java.net.URL
import com.beust.klaxon.Parser
import io.pleo.antaeus.core.exceptions.CurrencyInvalidException


class CurrencyConverter{

    companion object {
        private val WEB_SERVICE_URL = "https://api.exchangeratesapi.io/latest"
    }

    // https://www.kotlinresources.com/library/klaxon/
    // Parse from String value :
    public fun convert(from: Currency, to: Currency, amount: BigDecimal): BigDecimal {
        val response = URL(WEB_SERVICE_URL + "?base=${from.name}&symbols=${to.name}")
                .openStream()
                .bufferedReader()
                .use { it.readText() }
        val parser: Parser = Parser.default()
        val stringBuilder: StringBuilder = StringBuilder(response)
        val json: JsonObject = parser.parse(stringBuilder) as JsonObject
        if (json.obj("error").toString() == null){
            throw CurrencyInvalidException(from)
        }
        val rate = json.obj("rates")!!.double(to.name)!!.toBigDecimal()
        return rate * amount
    }
}