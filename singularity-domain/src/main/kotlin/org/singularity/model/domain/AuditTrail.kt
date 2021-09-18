package org.singularity.model.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.singularity.model.util.DateTimeSerializer
import java.time.LocalDateTime
import java.util.*

@Serializable
data class AuditTrail(
    val actionedBy: String,
    val changes: List<String>,
    val reason: String? = null,
    @Serializable(with = DateTimeSerializer::class) val dateTime: LocalDateTime = LocalDateTime.now(),
    @SerialName("id") override val _id: String = UUID.randomUUID().toString(),
)  :Entity {

    constructor(actionedBy: String, reason: String?) : this(actionedBy, listOf("DISABLE"),reason)



}
