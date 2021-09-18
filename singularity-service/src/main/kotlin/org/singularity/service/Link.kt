package org.singularity.service

import kotlinx.serialization.Serializable

@Serializable
data class Link(val rel:String, val link:String, val type:String)
