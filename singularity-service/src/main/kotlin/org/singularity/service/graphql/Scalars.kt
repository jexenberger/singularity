package org.singularity.service.graphql

import graphql.language.ScalarTypeDefinition
import graphql.schema.GraphQLScalarType


val DATE: GraphQLScalarType = GraphQLScalarType
    .newScalar()
    .name("Date")
    .coercing(DateCoercing())
    .definition(ScalarTypeDefinition("Date"))
    .build()

val DATE_TIME: GraphQLScalarType = GraphQLScalarType
    .newScalar()
    .name("DateTime")
    .coercing(DateTimeCoercing())
    .definition(ScalarTypeDefinition("DateTime"))
    .build()

