package org.singularity.model.domain

import java.time.LocalDateTime

class StateHistory(
    val dateTime: LocalDateTime,
    val oldState: Pair<String, String>?,
    val newState: Pair<String, String>
)