package org.singularity.model.domain

import kotlinx.serialization.Serializable

@Serializable
enum class Competency(val area:Area) {
    StakeHolderRepresentation(Area.Customer),
    Analysis(Area.Solution),
    Development(Area.Solution),
    Testing(Area.Solution),
    Leadership(Area.Endeavour),
    Management(Area.Endeavour)

}