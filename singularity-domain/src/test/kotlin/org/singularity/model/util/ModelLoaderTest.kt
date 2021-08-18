package org.singularity.model.util

import org.junit.jupiter.api.Test
import org.singularity.model.domain.AlphaName
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ModelLoaderTest {

    @Test
    internal fun loadModel() {
        val model = ModelLoader.loadAlpha(AlphaName.Stakeholders)
        assertNotNull(model.ref)
        assertNotNull(model.area)
        assertTrue { model.stateDefs.isNotEmpty() }
    }

    @Test
    internal fun loadAlpha() {
        val alpha = ModelLoader.createModel(AlphaName.Stakeholders);
        assertNotNull(alpha)
        println(ModelLoader.GSON.marshal(alpha))
    }

    @Test
    internal fun loadAllAlpha() {
        val alpha = ModelLoader.createModel();
        assertNotNull(alpha)
        println(ModelLoader.GSON.marshal(alpha))
    }
}