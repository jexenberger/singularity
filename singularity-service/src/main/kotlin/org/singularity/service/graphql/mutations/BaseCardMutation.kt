package org.singularity.service.graphql.mutations

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.singularity.model.domain.Alpha
import org.singularity.model.domain.Question
import org.singularity.model.domain.SoftwareSystem
import org.singularity.model.domain.State
import org.singularity.service.SystemService
import org.singularity.service.graphql.requiredCtxValue
import org.singularity.service.graphql.setCtxValue

abstract class BaseCardMutation(val service: SystemService) : DataFetcher<List<Question>> {

    override fun get(environment: DataFetchingEnvironment): List<Question> {

        val currentAlpha = environment.requiredCtxValue<Alpha>("alpha")
        val system = environment.requiredCtxValue<SoftwareSystem>("system")

        val state = if (environment.executionStepInfo.path.parent.isListSegment)
            currentAlpha.states[environment.executionStepInfo.path.parent.segmentIndex]!!
        else
            environment.requiredCtxValue("state")

        val newState = updateCard(environment, state)
        val newAlpha = currentAlpha.setState(newState)
        val softwareSystem = service.save(system.setAlpha(newAlpha), true)
        environment.setCtxValue("alpha", newAlpha)
        environment.setCtxValue("system", softwareSystem)
        return newState.card
    }

    protected abstract fun updateCard(
        environment: DataFetchingEnvironment,
        state: State
    ): State
}