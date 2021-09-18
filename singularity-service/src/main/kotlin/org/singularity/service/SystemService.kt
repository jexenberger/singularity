package org.singularity.service

import org.singularity.model.domain.AlphaName
import org.singularity.model.domain.SoftwareSystem
import org.singularity.model.domain.State
import org.singularity.model.domain.TeamMember

interface SystemService {

    fun save(system: SoftwareSystem, replace:Boolean = false): SoftwareSystem

    fun saveTeam(id: String, team: List<TeamMember>) =
        get(id)?.let { save(it.updateTeam(team)) }
            ?: throw IllegalArgumentException("$id is not a valid software system")

    fun saveTeamMember(id: String, teamMember: TeamMember) =
        get(id)?.let { save(it.updateTeam(teamMember)) }
            ?: throw IllegalArgumentException("$id is not a valid software system")

    fun removeTeamMember(systemId: String, id: String) =
        get(systemId)?.let { save(it.removeTeamMember(id)) }
            ?: throw IllegalArgumentException("$id is not a valid software system")

    fun activation(id: String, stateId: String, actionedBy: String, item: CheckListActivation): SoftwareSystem {
        val system = get(id) ?: throw IllegalArgumentException("${id} is not a valid software system")
        val newSystem = system.withState(stateId) {
            it.disableEnableCardItem(item.id, item.reason, actionedBy, item.state)
        }
        save(newSystem)
        return newSystem
    }

    fun get(id: String): SoftwareSystem?

    fun find(queryParameters: SystemQuery): SoftwareSystemPage

    fun getAlpha(id: String, alpha: AlphaName) = get(id)?.getAlpha(alpha)

    fun getState(id: String, stateId: String): State? = get(id)?.let {
        return it.getState(stateId)
    }


}