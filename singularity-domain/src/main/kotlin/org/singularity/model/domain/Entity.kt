package org.singularity.model.domain

interface Entity {
    val _id:String

    val id:String get() = _id
}
