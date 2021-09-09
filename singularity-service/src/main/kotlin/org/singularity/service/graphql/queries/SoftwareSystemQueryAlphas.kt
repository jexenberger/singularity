package org.singularity.service.graphql.queries

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.singularity.model.domain.Alpha
import org.singularity.model.domain.AlphaName
import org.singularity.model.domain.SoftwareSystem
import org.singularity.service.SystemService
import org.singularity.service.graphql.getCtxValue
import org.singularity.service.graphql.required
import org.singularity.service.graphql.requiredCtxValue
import org.singularity.service.graphql.setCtxValue

class SoftwareSystemQueryAlphas(val service:SystemService): DataFetcher<Alpha> {

    override fun get(environment: DataFetchingEnvironment): Alpha? {
        val name = AlphaName.valueOf(environment.arguments.required("name"))
        val alpha = environment.requiredCtxValue<SoftwareSystem>("system").getAlpha(name)
        environment.setCtxValue("alpha", alpha)
        return alpha
    }

}