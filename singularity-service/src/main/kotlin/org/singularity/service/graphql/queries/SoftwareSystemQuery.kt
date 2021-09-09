package org.singularity.service.graphql.queries

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.singularity.model.domain.Alpha
import org.singularity.model.domain.AlphaName
import org.singularity.model.domain.SoftwareSystem
import org.singularity.service.SystemService
import org.singularity.service.graphql.required
import org.singularity.service.graphql.setCtxValue
import java.time.LocalDate

val softwareSystem = SoftwareSystem("test", "test", "Acme inc", LocalDate.now())

class SoftwareSystemQuery(val service:SystemService) : DataFetcher<SoftwareSystem> {


    override fun get(environment: DataFetchingEnvironment): SoftwareSystem? {
        val id = environment.arguments.required<String>("id")
        return service.get(id)?.let {
            environment.setCtxValue("system", it)
            it
        }

    }

}