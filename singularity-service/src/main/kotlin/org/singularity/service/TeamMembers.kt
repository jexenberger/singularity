package org.singularity.service

import kotlinx.serialization.Serializable
import org.singularity.model.domain.TeamMember

@Serializable
class TeamMembers : ArrayList<TeamMember>() {
}