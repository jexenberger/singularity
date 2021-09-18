package org.singularity.service

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

class PageSerializer : JsonContentPolymorphicSerializer<AbstractPage>(AbstractPage::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out AbstractPage> {
        return SoftwareSystemPage.serializer()
    }
}
