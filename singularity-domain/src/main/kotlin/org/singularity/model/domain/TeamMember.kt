package org.singularity.model.domain

import java.util.*

data class TeamMember(
    val name: String,
    val organisation: String,
    val competency: List<Competency>,
    val email: String? = null,
    val number: String? = null,
    override val id: String = UUID.randomUUID().toString(),
) : Entity {


    fun update(name: String, organisation: String, competency: List<Competency>, email: String?, number: String?) : TeamMember {
        return TeamMember(name, organisation, competency, email, number)
    }


}