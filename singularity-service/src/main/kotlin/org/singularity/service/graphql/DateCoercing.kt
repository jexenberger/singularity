package org.singularity.service.graphql

import java.time.LocalDate


class DateCoercing : TemporalCoercing<LocalDate>() {

    override fun stringToDate(string: String): LocalDate = LocalDate.parse(string)


}