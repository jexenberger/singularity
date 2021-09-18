package org.singularity.model.util

import net.odoframework.container.GsonJson
import org.singularity.model.domain.Alpha
import org.singularity.model.domain.AlphaName
import org.singularity.model.domain.Question
import org.singularity.model.domain.State
import org.singularity.model.meta.AlphaDefinition
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.time.LocalDateTime

class ModelLoader {
    companion object {
        val GSON = GsonJson()
        val CACHE = mutableMapOf<AlphaName, AlphaDefinition>()

        fun loadAlpha(alpha: AlphaName): AlphaDefinition {
            if (CACHE.containsKey(alpha)) {
                return CACHE[alpha]!!
            }
            val out = ByteArrayOutputStream()
            ModelLoader::class.java.getResourceAsStream("/${alpha.name}.json")!!.copyTo(out)
            val json = out.toString(Charset.defaultCharset())
            val alphaDefinition = GSON.unmarshal(json, AlphaDefinition::class.java)
            CACHE[alpha] = alphaDefinition
            return alphaDefinition
        }

        fun createModel(): List<Alpha> =
            AlphaName.values().map {
                createModel(it)
            }.toList()


        fun createModel(alpha: AlphaName): Alpha {
            val metaModel = loadAlpha(alpha)
            val states = metaModel.stateDefs.mapIndexed { k, it ->
                val name = it.ref.split("/")[2]
                State(
                    _id = name.replace("/","::"),
                    ref = it.ref,
                    name = name,
                    description = it.description,
                    sequence = k.toShort(),
                    card = it.card.mapIndexed{ cnt, it ->
                        Question(
                            _id = cnt.toString(),
                            sequence = cnt.toShort(),
                            question = it,
                            enabled = false,
                            answer = false,
                            history = emptyList(),
                        )
                    }
                )
            }
            return Alpha(
                _id = alpha.name,
                name = alpha,
                ref = metaModel.ref,
                dateEstablished = LocalDateTime.now(),
                states = states
            ).calcState()
        }


    }


}