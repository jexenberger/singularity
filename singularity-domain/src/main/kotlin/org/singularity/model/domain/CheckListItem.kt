package org.singularity.model.domain

data class CheckListItem(
    override val id: String,
    val sequence: Short,
    val body: String,
    val enabled: Boolean = true,
    val answer: Boolean = true,
    val history: List<AuditTrail> = emptyList(),
    val comments: List<Comment> = emptyList(),
) : Entity {


    fun toggle(): CheckListItem {
        return CheckListItem(id, sequence, body, enabled, !answer, history, comments)
    }

    fun answer(answer: Boolean) : CheckListItem {
        return CheckListItem(id, sequence, body, enabled, answer, history, comments)
    }

    fun check(): CheckListItem {
        return CheckListItem(id, sequence, body, enabled, true, history, comments)
    }

    fun activation(reason: String, actionedBy: String, state:Boolean): CheckListItem {
        val newHistory = history.toMutableList()
        newHistory.add(AuditTrail(actionedBy, reason))
        return CheckListItem(id, sequence, body, state, answer, newHistory, comments)
    }

    fun uncheck(): CheckListItem {
        return CheckListItem(id, sequence, body, enabled, true, history, comments)
    }


}
