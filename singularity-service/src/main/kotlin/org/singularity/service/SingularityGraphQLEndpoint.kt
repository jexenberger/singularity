package org.singularity.service

import graphql.ExecutionInput
import graphql.execution.Execution
import net.odoframework.container.util.Json
import net.odoframework.service.InvocationContext
import net.odoframework.service.extensions.body
import net.odoframework.service.extensions.setCors
import net.odoframework.service.web.WebFunction
import net.odoframework.service.web.WebRequest
import net.odoframework.service.web.WebResponse
import org.singularity.service.graphql.SingularityGraphQL

class SingularityGraphQLEndpoint(val graphql: SingularityGraphQL, val jsonHandler:Json) : WebFunction {
    override fun apply(t: WebRequest, u: InvocationContext<*>): WebResponse {
        val query = t.body<String>()
        val execution = ExecutionInput.newExecutionInput(query).localContext(mutableMapOf<String, Any>()).build()
        val result = graphql.execute(execution)
        if (result.errors.isNotEmpty()) {
            val errors = result.errors.map { it.toSpecification() }
            return userError().setCors().body(errors)
        }
        return ok<Map<String, Any>>().setCors().body(result.getData<Map<String, Any>>())
    }

    override fun getJson() = jsonHandler


}