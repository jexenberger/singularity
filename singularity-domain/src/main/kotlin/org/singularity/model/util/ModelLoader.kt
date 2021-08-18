package org.singularity.model.util

import net.odoframework.container.GsonJson
import org.singularity.model.domain.Alpha
import org.singularity.model.domain.AlphaName
import org.singularity.model.domain.CheckListItem
import org.singularity.model.domain.State
import org.singularity.model.meta.AlphaDefinition
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.util.*

class ModelLoader {
    companion object {
        val GSON = GsonJson()

        fun loadAlpha(alpha: AlphaName): AlphaDefinition {
            val out = ByteArrayOutputStream()
            ModelLoader::class.java.getResourceAsStream("/${alpha.name}.json")!!.copyTo(out)
            val json = out.toString(Charset.defaultCharset())
            return GSON.unmarshal(json, AlphaDefinition::class.java)
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
                    id = name.replace("/","::"),
                    ref = it.ref,
                    name = name,
                    description = it.description,
                    sequence = k.toShort(),
                    card = it.card.mapIndexed{ cnt, it ->
                        CheckListItem(
                            id = cnt.toString(),
                            sequence = cnt.toShort(),
                            body = it,
                            enabled = true,
                            answer = false,
                            history = emptyList(),
                        )
                    }
                )
            }
            return Alpha(
                id = alpha.name,
                name = alpha,
                ref = metaModel.ref,
                dateEstablished = LocalDateTime.now(),
                states = states
            ).calcState()
        }


    }


}