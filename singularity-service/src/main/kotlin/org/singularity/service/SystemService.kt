package org.singularity.service

import org.singularity.model.domain.AlphaName
import org.singularity.model.domain.SoftwareSystem
import org.singularity.model.domain.State
import org.singularity.model.domain.TeamMember
import java.lang.IllegalArgumentException

interface SystemService {

    fun save(system: SoftwareSystem): String

    fun saveTeam(id:String, team: List<TeamMember>) =
        get(id)?.updateTeam(team) ?: throw IllegalArgumentException("$id is not a valid software system")


    fun activation(id:String, stateId:String, actionedBy:String, item:CheckListActivation)  : SoftwareSystem{
        val system = get(id) ?: throw IllegalArgumentException("${id} is not a valid software system")
        val newSystem = system.withState(stateId) {
            it.disableEnableCardItem(item.id, item.reason, actionedBy, item.state)
        }
        save(system)
        return system
    }

    fun get(id: String): SoftwareSystem?

    fun getAll(query: SystemQuery = SystemQuery()): List<SoftwareSystem>

    fun getAlpha(id: String, alpha: AlphaName) = get(id)?.getAlpha(alpha)

    fun getState(id: String, stateId: String) : State? = get(id)?.let {
       return it.getState(stateId)
    }


}