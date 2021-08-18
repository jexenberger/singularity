package org.singularity.model.domain

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.singularity.model.util.ModelLoader
import kotlin.test.*

class AlphaTest {

    var model = ModelLoader.createModel(AlphaName.SoftwareSystem)
    var card = model.states[0].card

    @BeforeEach
    internal fun setUp() {
        model = ModelLoader.createModel(AlphaName.SoftwareSystem)
        card = model.states[0].card
    }

    @Test
    internal fun setState() {
        val newCard = card.map { it.check() }
        val newAlpha = model.updateStateCard(model.states[0].id, newCard)
        val allChecked = newAlpha.getState(model.states[0].id)?.let {
            it.card.foldRight(true) { it, b ->
                b && it.answer
            }
        } ?: fail { "could not resolve a state which is in the hierarchy??!!" }
        assertTrue(allChecked, "the card should have been update with all answers true")
    }

    @Test
    internal fun calcState() {
        assertNull(model.currentState)
        val newCard = card.map { it.check() }
        val alpha = model.updateStateCard(model.states[0].id, newCard).calcState()
        assertTrue { alpha.currentState?.first.equals(model.states[0].id)  }
        val card2 = model.states[1].toggleCard().card
        val alpha2 = alpha.updateStateCard(model.states[1].id, card2).calcState()
        assertFalse { alpha.currentState?.equals(alpha2.currentState) ?: true }
    }

    @Test
    internal fun calcStateInvalid() {
        assertNull(model.currentState)
        val newCard = card.map { it.check() }
        val alpha = model.updateStateCard(model.states[0].id, newCard).calcState()
        val theState = alpha.currentState!!
        assertTrue { alpha.currentState?.first.equals(model.states[0].id)  }
        val card2 = model.states[2].toggleCard().card
        val alpha2 = alpha.updateStateCard(model.states[2].id, card2).calcState()
        assertEquals(theState, alpha2.currentState)
    }
}