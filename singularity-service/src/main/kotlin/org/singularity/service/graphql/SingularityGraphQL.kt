package org.singularity.service.graphql

import graphql.ExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring
import net.odoframework.util.Resource
import org.singularity.service.SystemService
import org.singularity.service.graphql.instrumentation.TrackingInstrumentation
import org.singularity.service.graphql.mutations.RemoveTeamMemberMutation
import org.singularity.service.graphql.mutations.SaveSoftwareSystemMutation
import org.singularity.service.graphql.mutations.SaveTeamMemberMutation
import org.singularity.service.graphql.mutations.UpdateCardMutation
import org.singularity.service.graphql.queries.SoftwareSystemCollectionQuery
import org.singularity.service.graphql.queries.SoftwareSystemQuery
import org.singularity.service.graphql.queries.SoftwareSystemQueryAlphas
import org.singularity.service.graphql.queries.StateQuery

class SingularityGraphQL(val service: SystemService, sdl: Resource) {

    private val graphQL: GraphQL

    init {
        val schemaParser = SchemaParser()
        val schemaGenerator = SchemaGenerator()
        val wiring = RuntimeWiring.newRuntimeWiring()
            .scalar(DATE)
            .scalar(DATE_TIME)
            .type("Query") { it.dataFetcher("save", SaveSoftwareSystemMutation(service)) }
            .type("SoftwareSystem") { it.dataFetcher("saveTeamMember", SaveTeamMemberMutation(service)) }
            .type("SoftwareSystem") { it.dataFetcher("removeTeamMember", RemoveTeamMemberMutation(service)) }
            .type("Query") { it.dataFetcher("byId", SoftwareSystemQuery(service)) }
            .type("Query") { it.dataFetcher("byFilter", SoftwareSystemCollectionQuery(service)) }
            .type("SoftwareSystem") { it.dataFetcher("alpha", SoftwareSystemQueryAlphas(service)) }
            .type("Alpha") { it.dataFetcher("state", StateQuery()) }
            .type("State") { it.dataFetcher("updateCard", UpdateCardMutation(service)) }
            .type(TypeRuntimeWiring.newTypeWiring("Label").typeResolver(LABEL).build())
            .build()

        val typeRegistry = schemaParser.parse(sdl.stream)
        val graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring)
        graphQL =  GraphQL
            .newGraphQL(graphQLSchema)
            .instrumentation(TrackingInstrumentation())
            .build()
    }


    fun execute(input: ExecutionInput) : ExecutionResult = graphQL.execute(input)

}