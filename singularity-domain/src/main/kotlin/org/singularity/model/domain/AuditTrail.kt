package org.singularity.model.domain

import java.time.LocalDateTime
import java.util.*

data class AuditTrail(
    val actionedBy: String,
    val changes: List<String>,
    val reason: String? = null,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val id:String = UUID.randomUUID().toString(),
) {

    constructor(actionedBy: String, reason: String?) : this(actionedBy, listOf("DISABLE"),reason)



}
