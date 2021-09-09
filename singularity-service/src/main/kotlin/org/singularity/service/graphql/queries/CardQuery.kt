package org.singularity.service.graphql.queries

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.singularity.model.domain.*
import java.time.LocalDate
import java.time.LocalDateTime

class CardQuery : DataFetcher<List<CheckListItem>> {
    override fun get(environment: DataFetchingEnvironment?): List<CheckListItem> {
        val alphaName = AlphaName.valueOf(environment!!.arguments["alphaName"].toString())
        return SoftwareSystem("test", "test","Acme inc", LocalDate.now()).getAlpha(alphaName).states[0].card;
    }
}