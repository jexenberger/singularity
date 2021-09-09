package org.singularity.service.graphql.instrumentation

import graphql.ExecutionResult
import graphql.execution.instrumentation.InstrumentationContext
import graphql.execution.instrumentation.SimpleInstrumentation
import graphql.execution.instrumentation.SimpleInstrumentationContext
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters
import graphql.execution.instrumentation.parameters.InstrumentationFieldParameters
import graphql.language.ListType
import graphql.language.NonNullType
import graphql.language.Type
import graphql.language.TypeName

val statePath = "^\\/.*\\/alphas\\/states\\[.{1,}\\]\\/.*\$".toRegex()

class TrackingInstrumentation : SimpleInstrumentation() {


    override fun beginField(parameters: InstrumentationFieldParameters): InstrumentationContext<ExecutionResult> {

        if (parameters.executionStepInfo.path.parent.isListSegment) {
            parameters.executionContext.getLocalContext<MutableMap<String, Any>>()["workingIndex"] = parameters.executionStepInfo.path.parent.segmentIndex
        }

        return object : SimpleInstrumentationContext<ExecutionResult>() {
        }
    }

    override fun beginFieldFetch(parameters: InstrumentationFieldFetchParameters): InstrumentationContext<Any> {
        return super.beginFieldFetch(parameters)
    }
}