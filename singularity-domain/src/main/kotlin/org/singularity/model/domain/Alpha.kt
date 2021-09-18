package org.singularity.model.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.singularity.model.util.DateTimeSerializer
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

@Serializable
data class Alpha(
    @SerialName("id") override val _id: String,
    val name:AlphaName,
    val ref: String,
    @Serializable(with = DateTimeSerializer::class) val dateEstablished: LocalDateTime,
    val states: List<State>,
    val currentState: Pair<String, String>? = null,
    val history: List<StateHistory> = emptyList()
) : Entity {

    fun getState(id:String) : State? =
        states.find { it._id.equals(id) }

    fun updateStateCard(id:String, card:List<Question>) : Alpha {
        val newState = getState(id)
            ?.updateCard(card)
            ?: throw IllegalArgumentException("$id is not a valid state for ${this.name}")
        return setState(newState)
    }


    fun setStates(states:List<State>) : Alpha {
        return Alpha(_id, name, ref, dateEstablished, states,currentState, history)
    }

    fun setState(state: State) : Alpha {
        val newList = states.map {
            if (it._id.equals(state._id)) state else it
        }
        return Alpha(_id,name,ref,dateEstablished,newList, currentState).calcState()
    }

    fun calcState() : Alpha {

        val sequence = states.fold(-1) { last, state ->
            if (state.achieved && (state.sequence - 1) == last) state.sequence.toInt() else last
        }

        if (sequence > -1) {
            val newState = states[sequence]._id to states[sequence].name
            if (!newState.equals(currentState)) {
                val newHistory = history.toMutableList()
                newHistory.add(StateHistory(LocalDateTime.now(), currentState, newState))
                return Alpha(_id, name, ref, dateEstablished, states, newState, newHistory)
            }
        }
        return this
    }



}
