package org.singularity.model.meta

import org.singularity.model.domain.Area

data class AlphaDefinition(
    val ref: String,
    val area: Area,
    val stateDefs: List<StateDefinition>
)