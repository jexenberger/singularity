package org.singularity.model.domain

data class State(
    override val id: String,
    val ref: String,
    val name: String,
    val description: String,
    val sequence: Short,
    val card: List<CheckListItem> = emptyList(),
    val history: List<StateHistory> = emptyList(),
    val riskFactor: Short = 1,
    val comments: List<Comment> = emptyList()
) : Entity {


    val achieved: Boolean
        get() {
            return card.foldRight(true) { a, b -> b && a.answer }
        }


    fun toggleCard() =
        updateCard(card.map { it.toggle() })

    fun checkCard() =
        updateCard(card.map { it.check() })

    fun uncheckCard() =
        updateCard(card.map { it.uncheck() })

    fun getCardItem(id: String): CheckListItem? = this.card.find { it.id == id }

    fun disableEnableCardItem(id: String, reason: String, actionedBy: String, state: Boolean): State {
        val newCard = card.map {
            if (it.id == id) it.activation(reason, actionedBy, state) else it
        }
        return updateCard(newCard)
    }

    fun updateCard(items: List<CheckListItem>): State {
        if (items.size < items.size) {
            throw IllegalArgumentException("items passed in are less than the existing size of the card")
        }
        val result = items.foldRight(true) { a, b ->
            b && card.find { a.id.equals(it.id) }?.let { true } ?: false
        }
        if (!result) throw IllegalArgumentException("new card included a question not in the original card")

        return State(id, ref, name, description, sequence, items, history, riskFactor, comments)
    }


    fun updateItems(updateItems: List<Pair<String, Boolean>>): State {
        val card = card.map {
            val existing = updateItems.find { update -> update.first == it.id }
            if (existing != null) it.answer(existing.second) else it

        }
        return State(id, ref, name, description, sequence, card, history, riskFactor, comments)
    }


}