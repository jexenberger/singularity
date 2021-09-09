package org.singularity.service.graphql.queries

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.singularity.model.domain.Alpha
import org.singularity.model.domain.State
import org.singularity.service.graphql.required
import org.singularity.service.graphql.requiredCtxValue
import org.singularity.service.graphql.setCtxValue

class StateQuery() : DataFetcher<State> {
    override fun get(environment: DataFetchingEnvironment): State {
        val alpha = environment.requiredCtxValue<Alpha>("alpha")
        val stateId = environment.required<String>("id")
        return alpha.getState(stateId)?.let {
            environment.setCtxValue("state", it)
            it
        } ?: throw IllegalArgumentException("$stateId not a valid state on ${alpha.name}")
    }
}