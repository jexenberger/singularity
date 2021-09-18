package org.singularity.model.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.singularity.model.util.DateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class State(
    @SerialName("id") override val _id: String,
    val ref: String,
    val name: String,
    val description: String,
    val sequence: Short,
    val card: List<Question> = emptyList(),
    val history: List<StateHistory> = emptyList(),
    val riskFactor: Short = 1,
    val comments: List<Comment> = emptyList(),
    @Serializable(with = DateTimeSerializer::class) val expiryDate: LocalDateTime? = null
) : Entity {


    val achieved: Boolean
        get() = card.foldRight(true) { a, b -> b && a.answer }

    val activeCard: List<Question>
        get() = card.filter { it.enabled }


    fun toggleCard() =
        updateCard(card.map { it.toggle() })


    fun disableEnableCardItem(id: String, reason: String, actionedBy: String, state: Boolean): State {
        val newCard = card.map {
            if (it._id == id) it.activation(reason, actionedBy, state) else it
        }
        return updateCard(newCard)
    }

    fun updateCard(items: List<Question>): State {
        if (items.size < items.size) {
            throw IllegalArgumentException("items passed in are less than the existing size of the card")
        }
        val result = items.foldRight(true) { a, b ->
            b && card.find { a._id.equals(it._id) }?.let { true } ?: false
        }
        if (!result) throw IllegalArgumentException("new card included a question not in the original card")

        return State(_id, ref, name, description, sequence, items, history, riskFactor, comments)
    }


    fun updateItems(who: String, updateItems: List<Pair<String, Boolean>>): State {
        val card = card.map {
            val existing = updateItems.find { update -> update.first == it._id }
            if (existing != null) it.answer(who, existing.second) else it
        }
        return State(_id, ref, name, description, sequence, card, history, riskFactor, comments)
    }

    fun expire(): State {
        val now = LocalDateTime.now();
        val card = if (expiryDate != null && (expiryDate.isBefore(now) || (expiryDate.isEqual(now)))) {
            card.map {
                it.answer("system", false)
            }
        } else {
            card
        }
        return State(_id, ref, name, description, sequence, card, history, riskFactor, comments)
    }


}