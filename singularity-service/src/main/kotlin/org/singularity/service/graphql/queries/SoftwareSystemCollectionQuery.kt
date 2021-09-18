package org.singularity.service.graphql.queries

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.singularity.model.domain.SoftwareSystem
import org.singularity.service.SystemQuery
import org.singularity.service.SystemService
import org.singularity.service.graphql.required
import org.singularity.service.graphql.valueAs

class SoftwareSystemCollectionQuery(val service: SystemService) : DataFetcher<List<SoftwareSystem>> {


    override fun get(environment: DataFetchingEnvironment): List<SoftwareSystem> {

        val size = environment.arguments.required<Int>("size")
        val page = environment.arguments.required<Int>("page")

        val query = environment.arguments.valueAs<Map<String, Any?>>("filter")?.let {
            SystemQuery(
                name = it["name"]?.let { s -> s as String },
                owner = it["owner"]?.let { s -> s as String },
                page = page,
                size = size
            )
        } ?: SystemQuery(
            page = page,
            size = size
        )

        return service.find(query).result

    }
}