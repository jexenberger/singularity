package org.singularity.model.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    @SerialName("id") override val _id: String,
    val sequence: Short,
    val question: String,
    val enabled: Boolean = true,
    val answer: Boolean = true,
    val history: List<AuditTrail> = emptyList(),
    val comments: List<Comment> = emptyList(),
) : Entity {


    fun toggle(): Question {
        return Question(_id, sequence, question, enabled, !answer, history, comments)
    }

    fun answer(who:String, answer: Boolean) : Question {
        val newHistory = ArrayList(history) + AuditTrail(actionedBy = who, reason = "Answer")
        return Question(_id, sequence, question, enabled, answer, newHistory, comments)
    }

    fun check(): Question {
        return Question(_id, sequence, question, enabled, true, history, comments)
    }

    fun activation(reason: String, actionedBy: String, state:Boolean): Question {
        val newHistory = history.toMutableList()
        newHistory.add(AuditTrail(actionedBy, reason))
        return Question(_id, sequence, question, state, answer, newHistory, comments)
    }

}
