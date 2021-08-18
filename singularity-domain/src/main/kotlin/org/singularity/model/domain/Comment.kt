package org.singularity.model.domain

import java.time.LocalDateTime

data class Comment(
    val dateTime: LocalDateTime,
    val comment:String
)
