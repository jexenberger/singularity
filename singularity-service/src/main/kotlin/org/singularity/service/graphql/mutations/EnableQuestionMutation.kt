package org.singularity.service.graphql.mutations

import graphql.schema.DataFetchingEnvironment
import org.singularity.model.domain.State
import org.singularity.service.SystemService
import org.singularity.service.graphql.required

class EnableQuestionMutation(service: SystemService) : BaseCardMutation(service) {

    override fun updateCard(environment: DataFetchingEnvironment, state: State): State {
        val id = environment.required<String>("id")
        val reason = environment.required<String>("reason")
        val who = environment.required<String>("who")
        val enabledOrDisabled = environment.required<Boolean>("enabledOrDisabled")
        return state.disableEnableCardItem(id, reason, who, enabledOrDisabled)
    }
}