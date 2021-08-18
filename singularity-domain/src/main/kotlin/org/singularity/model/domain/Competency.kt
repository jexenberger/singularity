package org.singularity.model.domain

enum class Competency(val area:Area) {
    StakeHolderRepresentation(Area.Customer),
    Analysis(Area.Solution),
    Development(Area.Solution),
    Testing(Area.Solution),
    Leadership(Area.Endeavour),
    Management(Area.Endeavour)

}