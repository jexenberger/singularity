package org.singularity.service.graphql

import java.time.LocalDateTime

class DateTimeCoercing : TemporalCoercing<LocalDateTime>() {
    override fun stringToDate(string: String): LocalDateTime = LocalDateTime.parse(string)
}