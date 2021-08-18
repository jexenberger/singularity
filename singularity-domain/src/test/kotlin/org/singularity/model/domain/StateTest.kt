package org.singularity.model.domain

import org.junit.jupiter.api.Test
import org.singularity.model.util.ModelLoader
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StateTest {

    val model = ModelLoader.createModel(AlphaName.Stakeholders)
    val card = model.states[0].card

    @Test
    internal fun setCard() {

        assertFalse { model.states[0].achieved }
        val newCard = card.map {
            it.toggle()
        }
        val updatedState = model.states[0].updateCard(newCard)

        newCard.forEachIndexed { k, it ->
            assertEquals(it.answer, updatedState.card[k].answer)
        }
        assertTrue { updatedState.achieved }
    }
}