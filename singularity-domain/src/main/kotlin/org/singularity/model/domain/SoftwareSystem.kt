package org.singularity.model.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.singularity.model.util.DateSerializer
import org.singularity.model.util.DateTimeSerializer
import org.singularity.model.util.ModelLoader
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.*
import kotlin.collections.ArrayList

@Serializable
data class SoftwareSystem(
    val name: String,
    val blurb: String,
    val owner: String,
    @Serializable(with = DateSerializer::class) val nextDeliverableDate: LocalDate? = null,
    val risk: Short = 0,
    val alphas: List<Alpha> = ModelLoader.createModel(),
    val team: List<TeamMember> = emptyList(),
    @Serializable(with = DateTimeSerializer::class) val createdDateTime: LocalDateTime = now(),
    override val _id: String = UUID.randomUUID().toString()

) : Entity {


    fun getAlpha(name: AlphaName) =
        alphas.find { it.name == name } ?: throw IllegalStateException("${name} should have been present")

    fun setAlpha(alpha: Alpha): SoftwareSystem {
        val newAlphas = alphas.map {
            if (it.name == alpha.name) alpha else it
        }
        return SoftwareSystem(name, blurb, owner, nextDeliverableDate, risk, newAlphas, team, createdDateTime, _id)
    }

    fun update(
        name: String = this.name,
        blurb: String = this.blurb,
        owner: String = this.owner,
        nextDeliverableDate: LocalDate? = this.nextDeliverableDate
    ): SoftwareSystem {
        return SoftwareSystem(name, blurb, owner, nextDeliverableDate, risk, alphas, team, createdDateTime, _id)
    }

    fun calcState(): SoftwareSystem {
        val alphas = alphas.map { it.calcState() }
        return SoftwareSystem(name, blurb, owner, nextDeliverableDate, risk, alphas, team, createdDateTime, _id)
    }

    fun updateTeam(team: List<TeamMember>): SoftwareSystem {
        return SoftwareSystem(name, blurb, owner, nextDeliverableDate, risk, alphas, ArrayList(team), createdDateTime, _id)
    }


    fun getTeamMemberById(id: String): TeamMember? = this.team.find { existing -> existing._id == id }

    fun removeTeamMember(id: String): SoftwareSystem {
        val teamMembers = this.team.filter { existing -> existing._id != id }
        return this.updateTeam(teamMembers)
    }

    fun updateTeam(teamMember: TeamMember): SoftwareSystem {
        var found = false
        val newTeam = this.team.map {
            if (it._id == teamMember._id) {
                found = true
                it.update(
                    teamMember.name,
                    teamMember.organisation,
                    teamMember.competency,
                    teamMember.email,
                    teamMember.number
                )
            } else {
               it
            }
        }

        return if (found) updateTeam(newTeam) else updateTeam(ArrayList(team) + teamMember)
    }

    fun withState(id: String, handler: (State) -> State): SoftwareSystem {
        val newAlphas = mutableListOf<Alpha>()
        for (alpha in this.alphas) {
            val newStates = mutableListOf<State>()
            for (state in alpha.states) {
                if (state._id == id) {
                    newStates.add(handler(state))
                } else {
                    newStates.add(state)
                }
            }
            newAlphas.add(alpha.setStates(newStates))
        }
        return SoftwareSystem(name, blurb, owner, nextDeliverableDate, risk, newAlphas, team, createdDateTime)
    }

    fun getState(id: String): State? {
        for (alpha in this.alphas) {
            val state = alpha.getState(id)
            if (state != null) {
                return state!!
            }
        }
        return null;
    }


}