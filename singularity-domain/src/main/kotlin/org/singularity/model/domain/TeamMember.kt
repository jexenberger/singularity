package org.singularity.model.domain

data class TeamMember(
    override val id:String,
    val name:String,
    val organisation:String,
    val competency: List<Competency>,
    val email:String? = null,
    val number:String? = null) : Entity