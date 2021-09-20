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

    @Test
    internal fun testMarshalMap() {
        val json = KotlinXSerializationJson()
        val result = json.marshal(mapOf(1 to 1, 2 to 2))
        assertNotNull(result)
    }
}