package org.singularity.service.graphql.mutations

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.singularity.model.domain.Alpha
import org.singularity.model.domain.CheckListItem
import org.singularity.model.domain.SoftwareSystem
import org.singularity.service.SystemService
import org.singularity.service.graphql.required
import org.singularity.service.graphql.requiredCtxValue
import org.singularity.service.graphql.setCtxValue

class UpdateCardMutation(val service:SystemService) : DataFetcher<List<CheckListItem>> {
    override fun get(environment: DataFetchingEnvironment): List<CheckListItem> {

        val currentAlpha = environment.requiredCtxValue<Alpha>("alpha")
        val system = environment.requiredCtxValue<SoftwareSystem>("system")

        val state = if (environment.executionStepInfo.path.parent.isListSegment)
            currentAlpha.states[environment.executionStepInfo.path.parent.segmentIndex]!!
        else
            environment.requiredCtxValue("state")

        val cardUpdate = environment.required<List<Map<String, Any>>>("newCard")

        val items = cardUpdate.map {
            val id = it.required<String>("id")
            val answer = it.required<Boolean>("answer")
            id to answer
        }
        val newState = state.updateItems(items)
        val newAlpha = currentAlpha.setState(newState)
        val softwareSystem = service.save(system.setAlpha(newAlpha), true)
        environment.setCtxValue("alpha", newAlpha)
        environment.setCtxValue("system", softwareSystem)
        return newState.card
    }
}