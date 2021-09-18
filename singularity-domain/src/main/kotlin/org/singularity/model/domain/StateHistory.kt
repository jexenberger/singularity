package org.singularity.model.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.singularity.model.util.DateTimeSerializer
import java.time.LocalDateTime

@Serializable
class StateHistory(
    @Serializable(with = DateTimeSerializer::class) val dateTime: LocalDateTime,
    val oldState: Pair<String, String>?,
    val newState: Pair<String, String>
)