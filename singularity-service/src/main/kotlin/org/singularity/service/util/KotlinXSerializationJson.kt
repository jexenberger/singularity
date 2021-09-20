package org.singularity.service.util

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.serializer
import net.odoframework.container.GsonJson
import net.odoframework.container.util.Json

class KotlinXSerializationJson : Json {

    private val json = kotlinx.serialization.json.Json {
        prettyPrint = true
        classDiscriminator = "#class"
    }

    private val gson = GsonJson()


    override fun marshal(obj: Any) : String {
        if (obj is Map<*,*>) {
            return gson.marshal(obj)
        }

        return json.encodeToString(serializer(obj::class.java), obj)
    }

    override fun <T : Any?> unmarshal(json: String, type: Class<T>) : T {
        val jsonStructure = this.json.parseToJsonElement(json)
        if (jsonStructure is JsonArray) {
            return this.json.decodeFromString(serializer(type), json) as T
        }
        return this.json.decodeFromString(serializer(type), json) as T
    }
}