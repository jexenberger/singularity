package org.singularity.service.graphql.queries

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.singularity.model.domain.*
import org.singularity.service.graphql.requiredCtxValue
import java.time.LocalDate

class ActiveCardQuery : DataFetcher<List<Question>> {
    override fun get(environment: DataFetchingEnvironment): List<Question> {
        val state = environment.requiredCtxValue<State>("state")
        return state.activeCard
    }
}