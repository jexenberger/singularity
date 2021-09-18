package org.singularity.service.graphql.mutations

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.singularity.model.domain.Alpha
import org.singularity.model.domain.Question
import org.singularity.model.domain.SoftwareSystem
import org.singularity.model.domain.State
import org.singularity.service.SystemService
import org.singularity.service.graphql.required
import org.singularity.service.graphql.requiredCtxValue
import org.singularity.service.graphql.setCtxValue

class UpdateCardMutation(service:SystemService) : BaseCardMutation(service) {

    override fun updateCard(
        environment: DataFetchingEnvironment,
        state: State
    ): State {
        val cardUpdate = environment.required<List<Map<String, Any>>>("newCard")
        val who = environment.required<String>("who")

        val items = cardUpdate.map {
            val id = it.required<String>("id")
            val answer = it.required<Boolean>("answer")
            id to answer
        }
        val newState = state.updateItems(who, items)
        return newState
    }
}