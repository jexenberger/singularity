package org.singularity.model.domain

import java.lang.IllegalArgumentException
import java.time.LocalDateTime

data class Alpha(
    override val id: String,
    val name:AlphaName,
    val ref: String,
    val dateEstablished: LocalDateTime,
    val states: List<State>,
    val currentState: Pair<String, String>? = null,
    val history: List<StateHistory> = emptyList()
) : Entity {



    fun getState(id:String) : State? =
        states.find { it.id.equals(id) }



    fun updateStateCard(id:String, card:List<CheckListItem>) : Alpha {
        val newState = getState(id)
            ?.updateCard(card)
            ?: throw IllegalArgumentException("$id is not a valid state for ${this.name}")
        return setState(newState)
    }


    fun setStates(states:List<State>) : Alpha {
        return Alpha(id, name, ref, dateEstablished, states,currentState, history)
    }

    fun setState(state: State) : Alpha {
        val newList = states.map {
            if (it.id.equals(state.id)) state else it
        }
        return Alpha(id,name,ref,dateEstablished,newList, currentState).calcState()
    }

    fun calcState() : Alpha {

        val sequence = states.fold(-1) { last, state ->
            if (state.achieved && (state.sequence - 1) == last) state.sequence.toInt() else last
        }

        if (sequence > -1) {
            val newState = states[sequence].id to states[sequence].name
            if (!newState.equals(currentState)) {
                val newHistory = history.toMutableList()
                newHistory.add(StateHistory(LocalDateTime.now(), currentState, newState))
                return Alpha(id, name, ref, dateEstablished, states, newState, newHistory)
            }
        }
        return this
    }



}
