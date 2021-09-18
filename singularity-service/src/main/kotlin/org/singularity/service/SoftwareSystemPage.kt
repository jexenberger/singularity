package org.singularity.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.singularity.model.domain.SoftwareSystem

@Serializable
data class SoftwareSystemPage(override val page:Int, val result:List<SoftwareSystem>, override val links:List<Link> = emptyList()) : AbstractPage() {
}