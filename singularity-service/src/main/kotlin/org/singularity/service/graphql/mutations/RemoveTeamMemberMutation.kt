package org.singularity.service.graphql.mutations

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.singularity.model.domain.Competency
import org.singularity.model.domain.SoftwareSystem
import org.singularity.model.domain.TeamMember
import org.singularity.service.SystemService
import org.singularity.service.graphql.required

class RemoveTeamMemberMutation(val service: SystemService) : DataFetcher<SoftwareSystem> {
    override fun get(environment: DataFetchingEnvironment): SoftwareSystem {
        val id = environment.required<String>("id")
        val systemId = environment.required<String>("systemId")
        return service.removeTeamMember(systemId, id)
    }
}