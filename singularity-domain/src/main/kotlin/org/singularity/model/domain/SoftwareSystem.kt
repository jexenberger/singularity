package org.singularity.model.domain

import org.singularity.model.util.ModelLoader
import java.lang.IllegalStateException
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.*

data class SoftwareSystem(
    val name: String,
    val blurb: String,
    val owner: String,
    val nextDeliverableDate: LocalDateTime?,
    val risk: Short = 0,
    val alphas: List<Alpha> = ModelLoader.createModel(),
    val team: List<TeamMember> = emptyList(),
    val createdDateTime: LocalDateTime = now(),
    override val id: String = UUID.randomUUID().toString()
) : Entity {


    fun getAlpha(name: AlphaName) =
       alphas.find { it.name == name } ?: throw IllegalStateException("${name} should have been present")

    fun setAlpha(alpha: Alpha) : SoftwareSystem {
        val newAlphas = alphas.map {
            if (it.id == alpha.id) alpha else it
        }
        return SoftwareSystem(name, blurb, owner, nextDeliverableDate, risk, newAlphas, team, createdDateTime, id)
    }

    fun update(name: String,
               blurb: String,
               owner: String,
               nextDeliverableDate: LocalDateTime?) : SoftwareSystem {
        return SoftwareSystem(name, blurb, owner, nextDeliverableDate, risk, alphas, team, createdDateTime, id)
    }

    fun calcState() : SoftwareSystem {
        val alphas = alphas.map { it.calcState() }
        return SoftwareSystem(name, blurb, owner, nextDeliverableDate, risk, alphas, team, createdDateTime, id)
    }

    fun updateTeam(team: List<TeamMember>) : SoftwareSystem {
        return SoftwareSystem(name, blurb, owner, nextDeliverableDate, risk, alphas, team, createdDateTime, id)
    }

    fun withState(id:String, handler: (State) -> State) : SoftwareSystem {
        val newAlphas = mutableListOf<Alpha>()
        for (alpha in this.alphas) {
            val newStates = mutableListOf<State>()
            for (state in alpha.states) {
                if (state.id == id) {
                    newStates.add(handler(state))
                } else {
                    newStates.add(state)
                }
            }
            newAlphas.add(alpha.setStates(newStates))
        }
        return SoftwareSystem(name, blurb, owner,nextDeliverableDate, risk,newAlphas,team,createdDateTime)
    }

    fun getState(id:String) : State? {
        for (alpha in this.alphas) {
            val state = alpha.getState(id)
            if (state != null) {
                return state!!
            }
        }
        return null;
    }



}