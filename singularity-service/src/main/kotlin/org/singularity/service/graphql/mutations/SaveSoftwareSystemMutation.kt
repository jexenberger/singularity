package org.singularity.service.graphql.mutations

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.singularity.model.domain.Competency
import org.singularity.model.domain.SoftwareSystem
import org.singularity.model.domain.TeamMember
import org.singularity.service.SystemService
import org.singularity.service.graphql.required
import org.singularity.service.graphql.setCtxValue
import org.singularity.service.graphql.valueAs
import java.time.LocalDate
import java.util.*

class SaveSoftwareSystemMutation(val service: SystemService) : DataFetcher<SoftwareSystem> {

    override fun get(environment: DataFetchingEnvironment): SoftwareSystem {
        val input = environment.required<Map<String, Any>>("system")
        val name = input.required<String>(SoftwareSystem::name.name)
        val owner = input.required<String>(SoftwareSystem::owner.name)
        val blurb = input.required<String>(SoftwareSystem::blurb.name)
        val id = input.valueAs<String>(SoftwareSystem::id.name) ?: UUID.randomUUID().toString()
        val nextDeliveryDate = input.valueAs<LocalDate>(SoftwareSystem::nextDeliverableDate.name)

        val team = input
            .valueAs<List<Map<String, String>>>(SoftwareSystem::team.name)?.let {
                it.map { memberDetails ->
                    TeamMember(
                        name = memberDetails.required(TeamMember::name.name),
                        organisation = memberDetails.required(TeamMember::organisation.name),
                        competency = memberDetails.required<List<String>>(TeamMember::competency.name).map { Competency.valueOf(it) },
                        email = memberDetails.valueAs<String>(TeamMember::email.name),
                        number = memberDetails.valueAs<String>(TeamMember::number.name),
                        id = memberDetails.valueAs<String>(TeamMember::id.name) ?: UUID.randomUUID().toString()
                    )
                }
            } ?: emptyList()




        val save = service.save(SoftwareSystem(
            name = name,
            owner = owner,
            blurb = blurb,
            nextDeliverableDate = nextDeliveryDate,
            team = team,
            id = id
        ))
        environment.setCtxValue("system", save)
        return save
    }
}