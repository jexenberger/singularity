package org.singularity.model.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.singularity.model.util.DateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class Comment(
    @Serializable(with = DateTimeSerializer::class) val dateTime: LocalDateTime,
    val comment:String
)
