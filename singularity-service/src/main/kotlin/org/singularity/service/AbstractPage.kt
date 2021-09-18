package org.singularity.service

import kotlinx.serialization.Serializable

@Serializable()
sealed class AbstractPage() {
    abstract val page:Int
    abstract val links: List<Link>

}