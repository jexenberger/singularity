package org.singularity.service

import kotlinx.serialization.Serializable
import org.singularity.model.domain.TeamMember

@Serializable
class TeamPage(override val page:Int, val result:List<TeamMember>, override val links:List<Link> = emptyList()) : AbstractPage()