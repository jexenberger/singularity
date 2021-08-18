package org.singularity.service

data class SystemQuery(
    val name:String? = null,
    val owner:String? = null,
    val risk:Integer? = null,
    val pageNumber:Integer? = null,
    val pageSize:Integer? = null
)
