package org.singularity.service.graphql.mutations

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.singularity.model.domain.Competency
import org.singularity.model.domain.SoftwareSystem
import org.singularity.model.domain.TeamMember
import org.singularity.service.SystemService
import org.singularity.service.graphql.required
import org.singularity.service.graphql.requiredCtxValue
import org.singularity.service.graphql.valueAs
import java.util.*

class SaveTeamMemberMutation(val service: SystemService) : DataFetcher<TeamMember> {

    override fun get(environment: DataFetchingEnvironment): TeamMember {
        val input = environment.required<Map<String, Any>>("input")
        return this.saveTeamMember(environment.requiredCtxValue("system"),input)
    }

    private fun saveTeamMember(system:SoftwareSystem, input: Map<String, Any>): TeamMember {
        val name = input.required<String>(TeamMember::name.name)
        val organisation = input.required<String>(TeamMember::organisation.name)
        val competencies =
            input.required<List<Any>>(TeamMember::competency.name).let {
                it.map { Competency.valueOf(it.toString()) }
            }
        val email = input.valueAs<String>(TeamMember::email.name)
        val number = input.valueAs<String>(TeamMember::number.name)
        val id = input.valueAs<String>(TeamMember::id.name) ?: UUID.randomUUID().toString()
        val teamMember = TeamMember(name, organisation, competencies, email, number, id)
        service.saveTeamMember(system.id, teamMember)
        return teamMember
    }
}