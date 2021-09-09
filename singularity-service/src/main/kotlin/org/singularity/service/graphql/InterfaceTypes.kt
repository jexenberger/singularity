package org.singularity.service.graphql

import graphql.schema.TypeResolver
import org.singularity.model.domain.Comment

val LABEL = TypeResolver {
    val theObject = it.getObject<Any>()
    return@TypeResolver  when(theObject) {
        is Comment -> it.schema.getObjectType("Comment")
        is Pair<*,*> -> it.schema.getObjectType("StateType")
        else -> null
    }
}