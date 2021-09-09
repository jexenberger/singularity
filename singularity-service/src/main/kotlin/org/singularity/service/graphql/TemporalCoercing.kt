package org.singularity.service.graphql

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.time.temporal.Temporal

abstract class TemporalCoercing<T : Temporal> : Coercing<T, String> {
    protected abstract fun stringToDate(string: String): T

    override fun serialize(dataFetcherResult: Any?): String {
        return dataFetcherResult.toString()
    }

    override fun parseValue(input: Any?): T {
        try {
            return stringToDate(input.toString())
        } catch (e: DateTimeParseException) {
            throw CoercingParseValueException("cannot parse $input", e)
        }
    }

    override fun parseLiteral(input: Any?): T {
        try {
            return stringToDate((input as StringValue).value)
        } catch (e: DateTimeParseException) {
            throw CoercingParseLiteralException("cannot parse $input", e)
        }
    }
}