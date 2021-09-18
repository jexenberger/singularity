package org.singularity.service.util

import org.junit.jupiter.api.Test
import org.singularity.model.domain.SoftwareSystem
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class KotlinXSerializationJsonTest {


    @Test
    internal fun testMarshalUnmarshal() {
        val json = KotlinXSerializationJson()
        val result = json.marshal(SoftwareSystem("test", "blurb", "owner"))
        println(result)
        assertNotNull(result)
        val system = json.unmarshal(result, SoftwareSystem::class.java)
        assertEquals("test", system.name)
    }
}